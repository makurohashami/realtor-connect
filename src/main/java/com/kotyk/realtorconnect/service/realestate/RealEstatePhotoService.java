package com.kotyk.realtorconnect.service.realestate;

import com.kotyk.realtorconnect.annotation.datafilter.RealEstatesPhotoFiltered;
import com.kotyk.realtorconnect.config.RealEstateConfiguration;
import com.kotyk.realtorconnect.dto.file.FileUploadResponse;
import com.kotyk.realtorconnect.dto.realestate.photo.RealEstatePhotoDto;
import com.kotyk.realtorconnect.dto.realestate.photo.RealEstatePhotoUpdateDto;
import com.kotyk.realtorconnect.entity.realestate.RealEstate;
import com.kotyk.realtorconnect.entity.realestate.RealEstatePhoto;
import com.kotyk.realtorconnect.mapper.RealEstatePhotoMapper;
import com.kotyk.realtorconnect.repository.RealEstatePhotoRepository;
import com.kotyk.realtorconnect.repository.RealEstateRepository;
import com.kotyk.realtorconnect.service.file.FileParamsGenerator;
import com.kotyk.realtorconnect.service.file.FileService;
import com.kotyk.realtorconnect.util.exception.ActionNotAllowedException;
import com.kotyk.realtorconnect.util.exception.ResourceNotFoundException;
import com.kotyk.realtorconnect.util.exception.ValidationFailedException;
import com.kotyk.realtorconnect.util.validator.Validator;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RealEstatePhotoService {

    private static final String NOT_FOUND_BY_ID_MSG = "Real estate photo with id '%d' not found";
    private static final Long START_ORDER = 0L;

    private final RealEstatePhotoRepository realEstatePhotoRepository;
    private final RealEstatePhotoMapper realEstatePhotoMapper;
    private final RealEstateConfiguration realEstateConfiguration;
    private final RealEstateRepository realEstateRepository;
    private final Validator<MultipartFile> realEstatePhotoValidator;
    private final FileParamsGenerator fileParamsGenerator;
    private final FileService fileService;

    private String getExMessage(long id) {
        return String.format(NOT_FOUND_BY_ID_MSG, id);
    }

    @Transactional
    public List<RealEstatePhotoDto> create(long realEstateId, Set<MultipartFile> photosToAdd) {
        validatePhotos(photosToAdd);
        RealEstate realEstate = realEstateRepository.findById(realEstateId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(RealEstateService.NOT_FOUND_BY_ID_MSG, realEstateId)));
        Set<RealEstatePhoto> photos = realEstate.getPhotos() == null ? new HashSet<>() : realEstate.getPhotos();
        long maxPhotosCount = realEstateConfiguration.getPhoto().getMaxPhotosCount();
        long sum = photos.size() + photosToAdd.size();
        if (sum > maxPhotosCount) {
            throw new ActionNotAllowedException(String.format("Sum of existing photos and new photos (%s) bigger then max photos count per one real estate (%s)", sum, maxPhotosCount));
        }
        Map<String, Object> params = fileParamsGenerator.generateParamsForRealEstatePhoto(realEstate);
        photosToAdd.parallelStream().forEach(file -> {
            FileUploadResponse response = fileService.uploadFile(file, params);
            photos.add(toRealEstatePhoto(response, realEstate));
        });
        validateOrder(new ArrayList<>(photos));
        return realEstatePhotoMapper.toListDto(realEstatePhotoRepository.saveAll(photos));
    }

    private void validatePhotos(Set<MultipartFile> photos) {
        Map<MultipartFile, List<String>> validationResults = photos.parallelStream()
                .collect(Collectors.toMap(Function.identity(), realEstatePhotoValidator::validate));
        validationResults.entrySet().removeIf(entry -> entry.getValue().isEmpty());
        if (!validationResults.isEmpty()) {
            List<String> errorsList = validationResults.entrySet().stream()
                    .map(entry -> String.format("[file: %s, errors: %s]",
                            entry.getKey().getOriginalFilename(),
                            String.join("; ", entry.getValue())))
                    .collect(Collectors.toList());
            throw new ValidationFailedException("Some photos are not valid. Errors: " + String.join(", ", errorsList));
        }
    }

    private RealEstatePhoto toRealEstatePhoto(FileUploadResponse response, RealEstate realEstate) {
        return RealEstatePhoto.builder()
                .photoId(response.getFileId())
                .photo(response.getUrl())
                .isPrivate(false)
                .order(Long.MAX_VALUE)
                .realEstate(realEstate)
                .build();
    }

    @RealEstatesPhotoFiltered
    @Transactional(readOnly = true)
    @Cacheable(value = "getListPhotoDto", key = "#realEstateId")
    public List<RealEstatePhotoDto> readAll(long realEstateId) {
        return realEstatePhotoMapper.toListDto(
                realEstatePhotoRepository.findAllByRealEstateId(realEstateId)
        );
    }

    @Transactional
    public RealEstatePhotoDto update(long realEstatePhotoId, RealEstatePhotoUpdateDto photo) {
        RealEstatePhoto toUpdate = realEstatePhotoRepository.findById(realEstatePhotoId)
                .orElseThrow(() -> new ResourceNotFoundException(getExMessage(realEstatePhotoId)));
        return realEstatePhotoMapper.toDto(realEstatePhotoMapper.update(toUpdate, photo));
    }

    @Transactional
    public List<RealEstatePhotoDto> customiseOrder(long realEstateId, LinkedHashSet<Long> idsOrder) {
        Map<Long, RealEstatePhoto> photosById = realEstatePhotoRepository.findAllByRealEstateId(realEstateId)
                .stream().collect(Collectors.toMap(RealEstatePhoto::getId, Function.identity()));
        if (CollectionUtils.isEmpty(photosById)) {
            throw new ResourceNotFoundException("Photos not found");
        }
        if (idsOrder.size() != photosById.size() || !photosById.keySet().containsAll(idsOrder)) {
            throw new ActionNotAllowedException("The count of IDs does not match the actual count of IDs or the IDs do not match");
        }
        long order = START_ORDER;
        for (Long id : idsOrder) {
            photosById.get(id).setOrder(order++);
        }
        return realEstatePhotoMapper.toListDto(photosById.values().stream().toList());
    }

    @Transactional
    public void delete(long realEstatePhotoId) {
        realEstatePhotoRepository.deleteById(realEstatePhotoId);
    }

    @Transactional
    public void validateOrder(long realEstateId) {
        validateOrder(realEstatePhotoRepository.findAllByRealEstateId(realEstateId));
    }

    @Transactional
    void validateOrder(List<RealEstatePhoto> photos) {
        photos.sort(Comparator.comparing(RealEstatePhoto::getOrder));
        long order = START_ORDER;
        for (RealEstatePhoto photo : photos) {
            photo.setOrder(order++);
        }
    }

}
