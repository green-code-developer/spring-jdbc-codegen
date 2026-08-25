package jp.green_code.spring_jdbc_codegen.test_app.repository;

import org.springframework.stereotype.Repository;
import jp.green_code.spring_jdbc_codegen.test_app.repository.base.BaseNowAllExcludedRepository;

/**
 * Table: now_all_excluded
 */
@Repository
public class NowAllExcludedRepository extends BaseNowAllExcludedRepository {
    public NowAllExcludedRepository(RepositoryHelper helper) {
        super(helper);
    }
}