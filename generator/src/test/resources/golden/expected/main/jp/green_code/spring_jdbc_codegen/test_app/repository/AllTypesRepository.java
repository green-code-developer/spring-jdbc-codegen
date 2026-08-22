package jp.green_code.spring_jdbc_codegen.test_app.repository;

import org.springframework.stereotype.Repository;
import jp.green_code.spring_jdbc_codegen.test_app.repository.base.BaseAllTypesRepository;

/**
 * Table: all_types
 */
@Repository
public class AllTypesRepository extends BaseAllTypesRepository {
    public AllTypesRepository(RepositoryHelper helper) {
        super(helper);
    }
}