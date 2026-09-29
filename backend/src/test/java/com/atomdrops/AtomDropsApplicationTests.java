package com.atomdrops;

import com.atomdrops.repair.model.RepairRequest;
import com.atomdrops.repair.repository.RepairRequestRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class AtomDropsApplicationTests {

	@Autowired
	RepairRequestRepository repairRequestRepository;

	@Test
	void contextLoads() {
	}

	@Test
	void openRequestsExcludeTheExcludedCustomersOwnRequests() {
		List<RepairRequest> all = repairRequestRepository.findByStatus("OPEN");
		if (all.isEmpty()) return;

		Long excluded = all.get(0).getCustomer().getId();
		List<RepairRequest> filtered =
				repairRequestRepository.findByStatusAndCustomerIdNotOrderByEmergencyDescCreatedAtDesc("OPEN", excluded);

		assertNotNull(filtered);
		assertTrue(filtered.size() <= all.size() - 1,
				"the excluded customer's own OPEN request must be filtered out");
		assertFalse(filtered.stream().anyMatch(r -> r.getCustomer().getId().equals(excluded)),
				"no returned request may belong to the excluded customer");
	}

}
