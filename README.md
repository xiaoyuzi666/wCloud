# wCloud
``
123.57.92.221
``
----


AuthController loginRequest registerRequest request ResponseEntity.ok(authService.getUserProfile(token));报红

BackupController  createBackup restore BackupStatus ResponseEntity.ok(backupService.getBackupStatus(backupId));报红


StudyController request, pageable ResponseEntity.ok(studyService.getStudy(id)); ResponseEntity.ok(studyService.getStatistics(startDate, endDate));报红

JwtUtil parserBuilder secretKey报红

AuthServiceImpl userRepository.save(user); User user = userRepository.findByUsername(username)
.orElseThrow(() -> new RuntimeException("User not found"));
userRepository.save(user); invalidateToken报红

DicomServiceImpl studyRepository.save(study);报红

FileSystemStorageService  throw new BusinessException("Failed to clean storage", e);报红

ImageServiceImpl public class ImageServiceImpl implements ImageService {UploadProgress  request.getFormat(),报红

PatientServiceImpl findByPatient 报红

ReportServiceImpl public class ReportServiceImpl implements ReportService {报红

StudyServiceImpl Page<Study> spec, pageable return studyRepository.findById(id)
.orElseThrow(() -> new RuntimeException("Study not found"));
}报红

SystemConfigServiceImpl public class SystemConfigServiceImpl implements SystemConfigService { key报红
UserServiceImpl public class UserServiceImpl implements UserService { isEnabled SimpleGrantedAuthority::new userRepository.save(user); User user = userRepository.findById(userId)
.orElseThrow(() -> new RuntimeException("User not found"));
getPhone User user = userRepository.findById(userId)
.orElseThrow(() -> new RuntimeException("User not found"));
return convertToDTO(user);
} User user = userRepository.findById(userId)
.orElseThrow(() -> new RuntimeException("User not found"));
userRepository.save(user);  phone 报红

ImageService UploadProgress RotateRequest ImageFormat 报红