## 👏TherapyHome

---

- [UN의 지속가능발전목표](http://ncsd.go.kr/unsdgs?content=2)에 관련된 주제를 받게되었고, 저희팀은 루게릭병 환자와 중증환자를 위한 IoT 어플리케이션 서비스를 만들었습니다.
- 대전 코로나확산으로 인해 해커톤 이틀전 취소되었습니다.

## 📎링크

---

- **Github**

[jazzyfact/TherapyHome](https://github.com/jazzyfact/TherapyHome)

## ⏲️개발기간

---

- **개발기간 : 2020/02/07 ~ 2020/02/22**

## ✏️담당업무

---

- **테라피홈  집 제작**
- **아두이노**
    - 라즈베이파이로 홈 cctv 만들고 어플리케이션 연동
    - 조이스틱 마우스를 제작해 어플리케이션과 연동
    - LED 조명 끄고 키기

## 🎞️상세내용

---

### 사전모임

- [사전모임 영상 링크](https://www.youtube.com/watch?v=5liqjGBhUDQ&t=66s)

![https://s3-us-west-2.amazonaws.com/secure.notion-static.com/f771dc0b-13b5-411a-a1b3-e43cd3708214/KakaoTalk_20200208_171146637.jpg](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/f771dc0b-13b5-411a-a1b3-e43cd3708214/KakaoTalk_20200208_171146637.jpg)

- **최종작품**

![https://s3-us-west-2.amazonaws.com/secure.notion-static.com/1e9960c0-e2e8-44a9-9fb8-660d21747911/.png](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/1e9960c0-e2e8-44a9-9fb8-660d21747911/.png)

- **ThreapyHome 영상**
    
    [https://s3-us-west-2.amazonaws.com/secure.notion-static.com/3a24b8e1-3b2b-4c63-9ac5-4b0685fce93c/_.mp4](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/3a24b8e1-3b2b-4c63-9ac5-4b0685fce93c/_.mp4)
    

- **기획배경**
    - 침상에 누워 생활 할 수 밖에 없는 몸이 불편한 루게릭 또는 중증 환자와 그 환자를 간병하는 가족의 기본적인 생활을 보장하기 위해서 서비스를 기획함.
- **기획내용**
    - 환자가 자의적으로 페이스트레킹과 IoT를 결합한 어플리케이션을 통해 주변 사물을 직접 컨트롤 할 수 있도록 했음.

## 📱기능

---

- **환자**
    - 아이트래킹 / 조이스틱으로 보호자에게 문자보내기
    - IoT 가전 컨트롤
        - 조명, 공기청정기
    - 긴급호출
- **보호자**
    - 환자가 보내는 메세지의 키워드, 연락처 편집
    - 환자 문자 모아보기
    - 환자 건강 상태 보기
        - 체온, 온도/습도, 심박수
    - 환자의 심박수가 낮아지면 긴급 푸쉬 알림
    - 홈 CCTV로 실시간 환자 상태 확인 가능
- **의료진**
    - 담당 환자 리스트 확인
    - 환자 건강 상태 보기

## 🛠️사용기술

---

- Face Tracking API
- Volley
- Firebase FCM
- Arduino, Raspberry Pi 3
- MJPG-STREAMER

## 💡프로젝트 회고

---

- 초반에 5명의 팀원들과 회의를 할 때 의견을 하나로 모으는게 쉽지 않아, 회의하는 시간만 길어지게 되었고, 그러다보니 정작 개발을 하는 시간은 부족했었습니다. 이런 일들을 겪으며 회의를 통해 불필요한 일과 필요한 일에 대한 리스트를 나누었고, 리스트 순위에 따라 업무처리를 하게 되었습니다. 순차적으로 일을 처리하게 되다보니 업무 효율이 좋아지게 되었고, 그러니 자연스럽게 팀 분위기도 좋아졌습니다. 저는 이 경험을 통해 업무 우선 순위의 중요성과 커뮤니케이션의 중요성에 대해 배웠습니다.
- 직접 용산전자매장에 가서 아두이노 및 라즈베이파이 제품을 구매하고 개발을 했습니다. 아두이노와 라즈베이파이를 처음 접하여, 알지 못 했던 기초적인 부분부터 연습했고. 와이파이 통신을 통해 직접 안드로이드 프로젝트에 연결했습니다. 맨 처음 아무것도 모른 상태로 시작했지만 쉬운 부분부터 하나씩 만들어가다보니 결국엔 원하는 기능을 구현 할 수 있었습니다. 이러한 경험을 통해 처음 접하는 기술이어도 기초부터 하나씩 배워간다면 해결 할 수 있을거란 자신감을 얻었습니다.
