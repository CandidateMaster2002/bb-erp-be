import os
import re

def remove_from_file(filepath, pattern, replacement=''):
    with open(filepath, 'r', encoding='utf-8') as f:
        content = f.read()
    new_content = re.sub(pattern, replacement, content, flags=re.MULTILINE | re.DOTALL)
    with open(filepath, 'w', encoding='utf-8') as f:
        f.write(new_content)

# 1. Lead.java
remove_from_file('src/main/java/com/boltblazers/erp/leads/Lead.java',
    r'\s*@ManyToOne\(fetch = FetchType\.LAZY\)\s*@JoinColumn\(name = "stage_id"\)\s*private Stage stage;')

# 2. LeadResponse.java
remove_from_file('src/main/java/com/boltblazers/erp/leads/dto/LeadResponse.java',
    r'\s*private Long stageId;\s*private String stageName;')

# 3. LeadQuickAddRequest.java
remove_from_file('src/main/java/com/boltblazers/erp/leads/dto/LeadQuickAddRequest.java',
    r'\s*private String stage;')

# 4. LeadSearchCriteria.java
remove_from_file('src/main/java/com/boltblazers/erp/leads/dto/LeadSearchCriteria.java',
    r'\s*private Long stageId;')

# 5. DashboardResponse.java
remove_from_file('src/main/java/com/boltblazers/erp/leads/dto/DashboardResponse.java',
    r'\s*private Map<String, Long> leadsByStage;')

# 6. ReportController.java
remove_from_file('src/main/java/com/boltblazers/erp/leads/controller/ReportController.java',
    r'\s*@GetMapping\("/leads-by-stage"\)\s*public ResponseEntity<\?> getLeadsByStage\(.*?\)\s*\{.*?return ResponseEntity\.ok\(Map\.of\(\)\);\s*\}')

# 7. DashboardService.java
remove_from_file('src/main/java/com/boltblazers/erp/leads/service/DashboardService.java',
    r'\s*// Leads by stage.*?res\.setLeadsByStage\(leadsByStage\);')

# 8. LeadController.java
remove_from_file('src/main/java/com/boltblazers/erp/leads/controller/LeadController.java',
    r'\s*@PatchMapping\("/{id}/stage"\)\s*public ResponseEntity<LeadResponse> updateStage\(.*?\)\s*\{.*?return ResponseEntity\.ok\(leadService\.updateStage\(id, request\.get\("stageId"\)\)\);\s*\}')

# 9. MasterDataController.java
remove_from_file('src/main/java/com/boltblazers/erp/leads/controller/MasterDataController.java',
    r'import com\.boltblazers\.erp\.leads\.Stage;\s*')
remove_from_file('src/main/java/com/boltblazers/erp/leads/controller/MasterDataController.java',
    r'import com\.boltblazers\.erp\.leads\.StageRepository;\s*')
remove_from_file('src/main/java/com/boltblazers/erp/leads/controller/MasterDataController.java',
    r'\s*private final StageRepository stageRepository;')
remove_from_file('src/main/java/com/boltblazers/erp/leads/controller/MasterDataController.java',
    r', StageRepository stageRepository')
remove_from_file('src/main/java/com/boltblazers/erp/leads/controller/MasterDataController.java',
    r'\s*this\.stageRepository = stageRepository;')
remove_from_file('src/main/java/com/boltblazers/erp/leads/controller/MasterDataController.java',
    r'\s*// ==================== STAGES ====================\s*@GetMapping\("/stages"\)\s*public ResponseEntity<List<Stage>> getStages\(\)\s*\{\s*return ResponseEntity\.ok\(stageRepository\.findAll\(\)\);\s*\}')

# 10. LeadService.java
remove_from_file('src/main/java/com/boltblazers/erp/leads/service/LeadService.java',
    r'import com\.boltblazers\.erp\.leads\.Stage;\s*')
remove_from_file('src/main/java/com/boltblazers/erp/leads/service/LeadService.java',
    r'import com\.boltblazers\.erp\.leads\.StageRepository;\s*')
remove_from_file('src/main/java/com/boltblazers/erp/leads/service/LeadService.java',
    r'\s*private final StageRepository stageRepository;')
remove_from_file('src/main/java/com/boltblazers/erp/leads/service/LeadService.java',
    r', StageRepository stageRepository')
remove_from_file('src/main/java/com/boltblazers/erp/leads/service/LeadService.java',
    r'\s*this\.stageRepository = stageRepository;')
remove_from_file('src/main/java/com/boltblazers/erp/leads/service/LeadService.java',
    r'\s*// Stage mapping\s*if \(request\.getStage\(\) != null\)\s*\{\s*stageRepository\.findAll\(\)\.stream\(\)\s*\.filter\(s -> s\.getName\(\)\.equalsIgnoreCase\(request\.getStage\(\)\.trim\(\)\)\)\s*\.findFirst\(\)\s*\.ifPresent\(lead::setStage\);\s*\}')
remove_from_file('src/main/java/com/boltblazers/erp/leads/service/LeadService.java',
    r'\s*if \(criteria\.getStageId\(\) != null\)\s*\{\s*predicates\.add\(cb\.equal\(root\.join\("stage", JoinType\.LEFT\)\.get\("id"\), criteria\.getStageId\(\)\)\);\s*\}')
remove_from_file('src/main/java/com/boltblazers/erp/leads/service/LeadService.java',
    r'\s*public LeadResponse updateStage\(Long leadId, Long stageId\)\s*\{[\s\S]*?return mapToResponse\(lead, false\);\s*\}')
remove_from_file('src/main/java/com/boltblazers/erp/leads/service/LeadService.java',
    r'\s*if \(lead\.getStage\(\) != null\)\s*\{\s*res\.setStageId\(lead\.getStage\(\)\.getId\(\)\);\s*res\.setStageName\(lead\.getStage\(\)\.getName\(\)\);\s*\}')

# 11. LeadImportService.java
remove_from_file('src/main/java/com/boltblazers/erp/leads/service/LeadImportService.java',
    r'import com\.boltblazers\.erp\.leads\.StageRepository;\s*')
remove_from_file('src/main/java/com/boltblazers/erp/leads/service/LeadImportService.java',
    r'\s*private final StageRepository stageRepository;')
remove_from_file('src/main/java/com/boltblazers/erp/leads/service/LeadImportService.java',
    r',\s*StageRepository stageRepository')
remove_from_file('src/main/java/com/boltblazers/erp/leads/service/LeadImportService.java',
    r'\s*this\.stageRepository = stageRepository;')
remove_from_file('src/main/java/com/boltblazers/erp/leads/service/LeadImportService.java',
    r',\s*Long stageId')
remove_from_file('src/main/java/com/boltblazers/erp/leads/service/LeadImportService.java',
    r',\s*stageId')

# 12. ImportController.java
remove_from_file('src/main/java/com/boltblazers/erp/leads/controller/ImportController.java',
    r',\s*@RequestParam\(value = "stageId", required = false\) Long stageId')
remove_from_file('src/main/java/com/boltblazers/erp/leads/controller/ImportController.java',
    r',\s*stageId')

# 13. BatchImportProcessor.java
remove_from_file('src/main/java/com/boltblazers/erp/leads/service/BatchImportProcessor.java',
    r'import com\.boltblazers\.erp\.leads\.Stage;\s*')
remove_from_file('src/main/java/com/boltblazers/erp/leads/service/BatchImportProcessor.java',
    r'import com\.boltblazers\.erp\.leads\.StageRepository;\s*')
remove_from_file('src/main/java/com/boltblazers/erp/leads/service/BatchImportProcessor.java',
    r'\s*private final StageRepository stageRepository;')
remove_from_file('src/main/java/com/boltblazers/erp/leads/service/BatchImportProcessor.java',
    r',\s*StageRepository stageRepository')
remove_from_file('src/main/java/com/boltblazers/erp/leads/service/BatchImportProcessor.java',
    r'\s*this\.stageRepository = stageRepository;')
remove_from_file('src/main/java/com/boltblazers/erp/leads/service/BatchImportProcessor.java',
    r',\s*Long defaultStageId')
remove_from_file('src/main/java/com/boltblazers/erp/leads/service/BatchImportProcessor.java',
    r'\s*Stage defaultStage = null;\s*if \(defaultStageId != null\)\s*\{\s*defaultStage = stageRepository\.findById\(defaultStageId\)\.orElse\(null\);\s*\}\s*if \(defaultStage == null\)\s*\{\s*defaultStage = stageRepository\.findAll\(\)\.stream\(\)\s*\.filter\(s -> "New"\.equalsIgnoreCase\(s\.getName\(\)\)\)\s*\.findFirst\(\)\.orElse\(null\);\s*\}')
remove_from_file('src/main/java/com/boltblazers/erp/leads/service/BatchImportProcessor.java',
    r',\s*defaultStage')
remove_from_file('src/main/java/com/boltblazers/erp/leads/service/BatchImportProcessor.java',
    r',\s*Stage defaultStage')
remove_from_file('src/main/java/com/boltblazers/erp/leads/service/BatchImportProcessor.java',
    r'\s*// Match stage\s*String stageName = row\.get\("lead_status"\);\s*Stage stage = defaultStage;\s*if \(stageName != null && !stageName\.isBlank\(\)\)\s*\{\s*stage = stageRepository\.findAll\(\)\.stream\(\)\s*\.filter\(s -> s\.getName\(\)\.equalsIgnoreCase\(stageName\.trim\(\)\)\)\s*\.findFirst\(\)\.orElse\(defaultStage\);\s*\}\s*lead\.setStage\(stage\);')

print("Replacements done!")
