package jp.green_code.spring_jdbc_codegen.test_app.repository;

import org.springframework.stereotype.Repository;
import jp.green_code.spring_jdbc_codegen.test_app.repository.base.BaseTriggerTestRepository;

/**
 * Table: trigger_test
 */
@Repository
public class TriggerTestRepository extends BaseTriggerTestRepository {
    public TriggerTestRepository(RepositoryHelper helper) {
        super(helper);
    }
}