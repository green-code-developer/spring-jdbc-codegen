package jp.green_code.spring_jdbc_codegen.test_app.repository;

import org.springframework.stereotype.Repository;
import jp.green_code.spring_jdbc_codegen.test_app.repository.base.BaseOnlyPk3NowRepository;

/**
 * Table: only_pk3_now
 */
@Repository
public class OnlyPk3NowRepository extends BaseOnlyPk3NowRepository {
    public OnlyPk3NowRepository(RepositoryHelper helper) {
        super(helper);
    }
}