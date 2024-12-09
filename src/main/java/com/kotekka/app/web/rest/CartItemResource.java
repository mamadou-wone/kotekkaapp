package com.kotekka.app.web.rest;

import com.kotekka.app.domain.CartItem;
import com.kotekka.app.repository.CartItemRepository;
import com.kotekka.app.service.CartItemQueryService;
import com.kotekka.app.service.CartItemService;
import com.kotekka.app.service.criteria.CartItemCriteria;
import com.kotekka.app.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.kotekka.app.domain.CartItem}.
 */
@RestController
@RequestMapping("/api/cart-items")
public class CartItemResource {

    private static final Logger LOG = LoggerFactory.getLogger(CartItemResource.class);

    private static final String ENTITY_NAME = "cartItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CartItemService cartItemService;

    private final CartItemRepository cartItemRepository;

    private final CartItemQueryService cartItemQueryService;

    public CartItemResource(
        CartItemService cartItemService,
        CartItemRepository cartItemRepository,
        CartItemQueryService cartItemQueryService
    ) {
        this.cartItemService = cartItemService;
        this.cartItemRepository = cartItemRepository;
        this.cartItemQueryService = cartItemQueryService;
    }

    /**
     * {@code POST  /cart-items} : Create a new cartItem.
     *
     * @param cartItem the cartItem to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cartItem, or with status {@code 400 (Bad Request)} if the cartItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CartItem> createCartItem(@Valid @RequestBody CartItem cartItem) throws URISyntaxException {
        LOG.debug("REST request to save CartItem : {}", cartItem);
        if (cartItem.getId() != null) {
            throw new BadRequestAlertException("A new cartItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        cartItem = cartItemService.save(cartItem);
        return ResponseEntity.created(new URI("/api/cart-items/" + cartItem.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, cartItem.getId().toString()))
            .body(cartItem);
    }

    /**
     * {@code PUT  /cart-items/:id} : Updates an existing cartItem.
     *
     * @param id the id of the cartItem to save.
     * @param cartItem the cartItem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cartItem,
     * or with status {@code 400 (Bad Request)} if the cartItem is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cartItem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CartItem> updateCartItem(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CartItem cartItem
    ) throws URISyntaxException {
        LOG.debug("REST request to update CartItem : {}, {}", id, cartItem);
        if (cartItem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cartItem.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cartItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        cartItem = cartItemService.update(cartItem);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cartItem.getId().toString()))
            .body(cartItem);
    }

    /**
     * {@code PATCH  /cart-items/:id} : Partial updates given fields of an existing cartItem, field will ignore if it is null
     *
     * @param id the id of the cartItem to save.
     * @param cartItem the cartItem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cartItem,
     * or with status {@code 400 (Bad Request)} if the cartItem is not valid,
     * or with status {@code 404 (Not Found)} if the cartItem is not found,
     * or with status {@code 500 (Internal Server Error)} if the cartItem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CartItem> partialUpdateCartItem(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CartItem cartItem
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CartItem partially : {}, {}", id, cartItem);
        if (cartItem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cartItem.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cartItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CartItem> result = cartItemService.partialUpdate(cartItem);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cartItem.getId().toString())
        );
    }

    /**
     * {@code GET  /cart-items} : get all the cartItems.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cartItems in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CartItem>> getAllCartItems(
        CartItemCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get CartItems by criteria: {}", criteria);

        Page<CartItem> page = cartItemQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /cart-items/count} : count all the cartItems.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countCartItems(CartItemCriteria criteria) {
        LOG.debug("REST request to count CartItems by criteria: {}", criteria);
        return ResponseEntity.ok().body(cartItemQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /cart-items/:id} : get the "id" cartItem.
     *
     * @param id the id of the cartItem to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cartItem, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CartItem> getCartItem(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CartItem : {}", id);
        Optional<CartItem> cartItem = cartItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cartItem);
    }

    /**
     * {@code DELETE  /cart-items/:id} : delete the "id" cartItem.
     *
     * @param id the id of the cartItem to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCartItem(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CartItem : {}", id);
        cartItemService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
