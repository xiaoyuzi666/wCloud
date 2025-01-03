# wCloud
``
123.57.92.221
``
----


PatientController  findPatients ResponseEntity.ok(patientService.getPatient(id)); createPatient updatePatient deletePatient 报红

ReportController generateReport getReportsByStudy 报红

SystemConfigController getConfigValue setConfigValue initializeDefaultConfigs(); pageable getConfigsByCategory 报红

UserController ch8angePassword 报红

DicomServiceImpl public class DicomServiceImpl implements DicomService { void 报红

ReportServiceImpl public class ReportServiceImpl implements ReportService { setStudyId setTitle() setTitle getStudyId() getTitle() 报红

StudyServiceImpl studyRepository.getDailyStudyCount(startDate, endDate) description 报红

SystemConfigServiceImpl public class SystemConfigServiceImpl implements SystemConfigService { key 报红

UserServiceImpl public class UserServiceImpl implements UserService { isEnabled(), User = userRepository.findById(userId)
.orElseThrow(() -> new RuntimeException("User not found"));
ser user = userRepository.findById(userId)
.orElseThrow(() -> new RuntimeException("User not found"));
User user = userRepository.findById(userId)
.orElseThrow(() -> new RuntimeException("User not found"));
userRepository.save(user);报红