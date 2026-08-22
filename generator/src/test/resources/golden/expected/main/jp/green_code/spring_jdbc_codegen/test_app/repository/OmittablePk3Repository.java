package jp.green_code.spring_jdbc_codegen.test_app.repository;

import org.springframework.stereotype.Repository;
import jp.green_code.spring_jdbc_codegen.test_app.repository.base.BaseOmittablePk3Repository;

/**
 * Table: omittable_pk3
 */
@Repository
public class OmittablePk3Repository extends BaseOmittablePk3Repository {
    public OmittablePk3Repository(RepositoryHelper helper) {
        super(helper);
    }
}