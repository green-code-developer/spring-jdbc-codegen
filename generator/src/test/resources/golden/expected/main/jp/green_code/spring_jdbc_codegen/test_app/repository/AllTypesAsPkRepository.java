package jp.green_code.spring_jdbc_codegen.test_app.repository;

import org.springframework.stereotype.Repository;
import jp.green_code.spring_jdbc_codegen.test_app.repository.base.BaseAllTypesAsPkRepository;

/**
 * Table: all_types_as_pk
 */
@Repository
public class AllTypesAsPkRepository extends BaseAllTypesAsPkRepository {
    public AllTypesAsPkRepository(RepositoryHelper helper) {
        super(helper);
    }
}