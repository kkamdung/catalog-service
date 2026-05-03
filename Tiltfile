gradlew = './gradlew'
expected_ref = '$EXPECTED_REF'

if os.name == 'nt':
    gradlew = '.\\gradlew.bat'
    expected_ref = '%EXPECTED_REF%'

# Build
custom_build(
    ref = 'catalog-service',
    command = gradlew + ' bootBuildImage --imageName ' + expected_ref,
    deps = ['build.gradle', 'src']
)

# Deploy
k8s_yaml(['k8s/deployment.yml', 'k8s/service.yml'])

# Manage
k8s_resource('catalog-service', port_forwards=['9001'])
