package jp.green_code.spring_jdbc_codegen.test_app.repository;

import org.springframework.stereotype.Repository;
import jp.green_code.spring_jdbc_codegen.test_app.repository.base.BasePrimitiveDefaultRepository;

/**
 * Table: primitive_default
 */
@Repository
public class PrimitiveDefaultRepository extends BasePrimitiveDefaultRepository {
    public PrimitiveDefaultRepository(RepositoryHelper helper) {
        super(helper);
    }
}