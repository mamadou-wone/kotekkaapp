package com.kotekka.app.repository;

import com.kotekka.app.domain.WalletHolder;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the WalletHolder entity.
 */
@Repository
public interface WalletHolderRepository extends JpaRepository<WalletHolder, Long>, JpaSpecificationExecutor<WalletHolder> {
    default Optional<WalletHolder> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<WalletHolder> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<WalletHolder> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select walletHolder from WalletHolder walletHolder left join fetch walletHolder.user",
        countQuery = "select count(walletHolder) from WalletHolder walletHolder"
    )
    Page<WalletHolder> findAllWithToOneRelationships(Pageable pageable);

    @Query("select walletHolder from WalletHolder walletHolder left join fetch walletHolder.user")
    List<WalletHolder> findAllWithToOneRelationships();

    @Query("select walletHolder from WalletHolder walletHolder left join fetch walletHolder.user where walletHolder.id =:id")
    Optional<WalletHolder> findOneWithToOneRelationships(@Param("id") Long id);
}
