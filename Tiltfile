gradlew = './gradlew'
expected_ref = '$EXPECTED_REF'
if os.name == 'nt':
    gradlew = 'gradlew.bat'
    expected_ref = '%EXPECTED_REF%'


# 빌드
custom_build(
    # 컨테이너 이미지 이름
    ref = 'catalog-service',

    # 컨테이너 이미지를 빌드하기 위한 명령어
    # 윈도우는 %EXPECTED_REF%, 그 외 $EXPECTED_REF
    command = gradlew + ' bootBuildImage --imageName ' + expected_ref,

    # 새로운 빌드를 시작하기 위해 지켜봐야 할 파일
    deps = ['build.gradle', 'src']
)

# 배포
k8s_yaml(['k8s/deployment.yml', 'k8s/service.yml'])

# 관리
k8s_resource('catalog-service', port_forwards=['9001'])
