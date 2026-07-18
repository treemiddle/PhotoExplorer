# PhotoExplorer

## 기술 스택

| 분류 | 사용 |
| --- | --- |
| UI | Jetpack Compose, Navigation Compose, Coil |
| 프레젠테이션 | MVI |
| DI | Hilt |
| 네트워크 | Retrofit + OkHttp + kotlinx.serialization |
| 로컬 저장소 | Room, DataStore, 내부 파일 저장소 |
| 비동기 | Coroutines + Flow |

## 아키텍처

Domain을 중심으로 한 단방향 의존 구조입니다.

- `domain`은 외부 라이브러리에 의존하지 않는 순수 Kotlin 레이어입니다.
- `data`는 `domain`의 `Repository` 인터페이스를 구현합니다.
- `local`, `remote`는 `data`에서 정의한 `DataSource` 인터페이스를 구현합니다.

```text
app ──→ navigation ──→ feature ────────────→ domain ◀── data ◀── local / remote
 │                       │                     ▲
 │ (theme)               ▼                     │
 │                    core/ui ─────────────────┘
 │                       ┆
 └─────────────→ core/designsystem
                         ┆
                   core/compose
                         ┆
              core/model · core/common
```

### 패키지 구성

| 패키지 | 담당 |
| --- | --- |
| `feature` | Compose UI와 ViewModel, 화면 상태 및 이벤트 처리 |
| `navigation` | Navigation Route 및 NavHost 구성 |
| `core/ui` | MVI 베이스 클래스, `domain` → `core/model` 매핑 |
| `core/designsystem` | 공통 UI 컴포넌트 및 Theme |
| `core/compose` | Compose 공통 유틸리티 |
| `core/model` | Design System에서 사용하는 UI 모델 |
| `core/common` | 순수 Kotlin 공통 유틸리티 |
| `domain` | 비즈니스 모델 및 Repository 인터페이스 |
| `data` | Repository 구현, DataSource 인터페이스 정의 및 모델 매핑 |
| `local` | Room, DataStore, 내부 파일 저장소 |
| `remote` | Retrofit 기반 API 구현 |