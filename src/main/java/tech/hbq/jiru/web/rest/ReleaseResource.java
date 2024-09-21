package tech.hbq.jiru.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.hbq.jiru.repository.ReleaseRepository;
import tech.hbq.jiru.service.ReleaseService;
import tech.hbq.jiru.service.dto.ReleaseDTO;
import tech.hbq.jiru.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link tech.hbq.jiru.domain.Release}.
 */
@RestController
@RequestMapping("/api")
public class ReleaseResource {

    private final Logger log = LoggerFactory.getLogger(ReleaseResource.class);

    private static final String ENTITY_NAME = "release";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReleaseService releaseService;

    private final ReleaseRepository releaseRepository;

    public ReleaseResource(ReleaseService releaseService, ReleaseRepository releaseRepository) {
        this.releaseService = releaseService;
        this.releaseRepository = releaseRepository;
    }

    /**
     * {@code POST  /releases} : Create a new release.
     *
     * @param releaseDTO the releaseDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new releaseDTO, or with status {@code 400 (Bad Request)} if the release has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/releases")
    public ResponseEntity<ReleaseDTO> createRelease(@RequestBody ReleaseDTO releaseDTO) throws URISyntaxException {
        log.debug("REST request to save Release : {}", releaseDTO);
        if (releaseDTO.getId() != null) {
            throw new BadRequestAlertException("A new release cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ReleaseDTO result = releaseService.save(releaseDTO);
        return ResponseEntity
            .created(new URI("/api/releases/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /releases/:id} : Updates an existing release.
     *
     * @param id the id of the releaseDTO to save.
     * @param releaseDTO the releaseDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated releaseDTO,
     * or with status {@code 400 (Bad Request)} if the releaseDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the releaseDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/releases/{id}")
    public ResponseEntity<ReleaseDTO> updateRelease(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ReleaseDTO releaseDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Release : {}, {}", id, releaseDTO);
        if (releaseDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, releaseDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!releaseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ReleaseDTO result = releaseService.update(releaseDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, releaseDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /releases/:id} : Partial updates given fields of an existing release, field will ignore if it is null
     *
     * @param id the id of the releaseDTO to save.
     * @param releaseDTO the releaseDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated releaseDTO,
     * or with status {@code 400 (Bad Request)} if the releaseDTO is not valid,
     * or with status {@code 404 (Not Found)} if the releaseDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the releaseDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/releases/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ReleaseDTO> partialUpdateRelease(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ReleaseDTO releaseDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Release partially : {}, {}", id, releaseDTO);
        if (releaseDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, releaseDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!releaseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ReleaseDTO> result = releaseService.partialUpdate(releaseDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, releaseDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /releases} : get all the releases.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of releases in body.
     */
    @GetMapping("/releases")
    public ResponseEntity<List<ReleaseDTO>> getAllReleases(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Releases");
        Page<ReleaseDTO> page = releaseService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /releases/:id} : get the "id" release.
     *
     * @param id the id of the releaseDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the releaseDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/releases/{id}")
    public ResponseEntity<ReleaseDTO> getRelease(@PathVariable Long id) {
        log.debug("REST request to get Release : {}", id);
        Optional<ReleaseDTO> releaseDTO = releaseService.findOne(id);
        return ResponseUtil.wrapOrNotFound(releaseDTO);
    }

    /**
     * {@code DELETE  /releases/:id} : delete the "id" release.
     *
     * @param id the id of the releaseDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/releases/{id}")
    public ResponseEntity<Void> deleteRelease(@PathVariable Long id) {
        log.debug("REST request to delete Release : {}", id);
        releaseService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
