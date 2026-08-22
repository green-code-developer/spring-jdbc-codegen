package jp.green_code.spring_jdbc_codegen.test_app.repository;

import org.springframework.stereotype.Repository;
import jp.green_code.spring_jdbc_codegen.test_app.repository.base.BaseNormalPk3Repository;

/**
 * Table: normal_pk3
 */
@Repository
public class NormalPk3Repository extends BaseNormalPk3Repository {
    public NormalPk3Repository(RepositoryHelper helper) {
        super(helper);
    }
}