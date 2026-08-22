package jp.green_code.spring_jdbc_codegen.test_app.repository;

import org.springframework.stereotype.Repository;
import jp.green_code.spring_jdbc_codegen.test_app.repository.base.BaseOnlyPk3Repository;

/**
 * Table: only_pk3
 */
@Repository
public class OnlyPk3Repository extends BaseOnlyPk3Repository {
    public OnlyPk3Repository(RepositoryHelper helper) {
        super(helper);
    }
}