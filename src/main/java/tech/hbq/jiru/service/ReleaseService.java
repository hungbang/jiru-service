package tech.hbq.jiru.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.hbq.jiru.domain.Release;
import tech.hbq.jiru.repository.ReleaseRepository;
import tech.hbq.jiru.service.dto.ReleaseDTO;
import tech.hbq.jiru.service.mapper.ReleaseMapper;

/**
 * Service Implementation for managing {@link Release}.
 */
@Service
@Transactional
public class ReleaseService {

    private final Logger log = LoggerFactory.getLogger(ReleaseService.class);

    private final ReleaseRepository releaseRepository;

    private final ReleaseMapper releaseMapper;

    public ReleaseService(ReleaseRepository releaseRepository, ReleaseMapper releaseMapper) {
        this.releaseRepository = releaseRepository;
        this.releaseMapper = releaseMapper;
    }

    /**
     * Save a release.
     *
     * @param releaseDTO the entity to save.
     * @return the persisted entity.
     */
    public ReleaseDTO save(ReleaseDTO releaseDTO) {
        log.debug("Request to save Release : {}", releaseDTO);
        Release release = releaseMapper.toEntity(releaseDTO);
        release = releaseRepository.save(release);
        return releaseMapper.toDto(release);
    }

    /**
     * Update a release.
     *
     * @param releaseDTO the entity to save.
     * @return the persisted entity.
     */
    public ReleaseDTO update(ReleaseDTO releaseDTO) {
        log.debug("Request to update Release : {}", releaseDTO);
        Release release = releaseMapper.toEntity(releaseDTO);
        release = releaseRepository.save(release);
        return releaseMapper.toDto(release);
    }

    /**
     * Partially update a release.
     *
     * @param releaseDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ReleaseDTO> partialUpdate(ReleaseDTO releaseDTO) {
        log.debug("Request to partially update Release : {}", releaseDTO);

        return releaseRepository
            .findById(releaseDTO.getId())
            .map(existingRelease -> {
                releaseMapper.partialUpdate(existingRelease, releaseDTO);

                return existingRelease;
            })
            .map(releaseRepository::save)
            .map(releaseMapper::toDto);
    }

    /**
     * Get all the releases.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ReleaseDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Releases");
        return releaseRepository.findAll(pageable).map(releaseMapper::toDto);
    }

    /**
     * Get one release by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ReleaseDTO> findOne(Long id) {
        log.debug("Request to get Release : {}", id);
        return releaseRepository.findById(id).map(releaseMapper::toDto);
    }

    /**
     * Delete the release by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Release : {}", id);
        releaseRepository.deleteById(id);
    }
}
