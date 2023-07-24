

# HelloFood

프로젝트 명 : Hello Food - 냉장고를 부탁해

# 선정 이유

전 세계적으로 매년 수십억 톤의 음식이 낭비되고 있는데 그 중 상당 부분 중 하나가 냉장고에 남은 식재료들입니다.
식재료들이 버려지는 이유는
바쁜 현대 사회 속에서 요리할 시간이 없고 할 시간이 있더라도 요리할 방법을 모르거나,
한번 사용하고 넣어둔 식재료의 유통기한을 파악하지 못하여 상하거나,
냉장고 속에 넣어두고 깜빡하여 오랫동안 놔두고 나중에 발견하여 버리는 경우가 있습니다.

그래서 Hello World팀은 이러한 문제점을 해결하기 위해
냉장고의 식재료들을 정리할 수 있는 냉장고 저장공간을 만들어 냉장고 속에 어떤 재료가 남아있는지 확인할 수 있도록 하여 깜빡하고 버리는 경우를 줄이고,
남은 재료로 쉽게 요리할 수 있는 레시피를 추천하고 요리 순서를 알려주어 요리를 모르는 사람들도 손쉽게 요리에 접근할 수 있도록 하고,
유통기한 설정으로 유통기한이 얼마남지 않은 식재료들을 알려줘서 식재료가 낭비되는 상황을 최대한 줄이는 해결방안을 중점으로 본 프로젝트를 선정했습니다.

# 기대 효과

1. 레시피 추천을 통해 사람들이 냉장고에 남아있는 식재료를 사용하여 식재료 구매에 쓰이는 금액을 절약하고 요리의 즐거움으로 생활 속 소확행을 얻습니다.


2. 유통기한 알림 기능을 통해 식재료를 잊거나 버리는 경우를 줄여 음식물 쓰레기가 늘어나는 요인을 줄여 환경 보존에 약간 기여합니다.


3. 냉장고 속 식재료를 확인할 수 있어 식재료 구매 시 부족하거나 필요한 것들만 구매하여 금전적 절약을 도웁니다.
   

# 벤치마킹 사이트

[만개의 레시피](https://www.10000recipe.com/ranking/home_new.html) :

소지한 식자재를 통해 레시피를 찾아볼 수 있다는 점에서 해당 사이트를 벤치마킹 했습니다. 본 프로젝트와 해당 사이트의 차별점으로는 본 프로젝트의 경우, Open AI를 통해 레시피를 제공하는 기능을 추가했습니다. 고객들은 이를 통해 웹서핑에 시간을 허비할 필요없이, 즉각적으로 레시피를 제공받을 수 있습니다.


[냉장고파먹기(앱)](https://apps.apple.com/kr/app/%EB%83%89%EC%9E%A5%EA%B3%A0-%ED%8C%8C%EB%A8%B9%EA%B8%B0/id1623066651) :

냉장고 안의 식자재를 앱 상에 등록하여 정보를 파악할 수 있다는 점에서 해당 어플리케이션을 벤치마킹했습니다. 해당 앱과 본 프로젝트의 차별점으로는 본 프로젝트의 경우 보다 상세한 식자재 관리가 가능합니다.. 냉장고 파먹기 어플리케이션의 경우, 단순한 신선도 체크 기능만을 제공합니다. 하지만, 저희는 유통기한(혹은 소비기한) 등록을 통한 냉장고 관리,  레시피에 따른 식자재 소비체크를 통한 재고의 자동관리 등을 제공하고자 합니다.

# 사용 예시
<br>
<br>
<br>
<br>
<img src="image/getRecipe.gif"/>
<br>
<br>
<h3>API를 이용한 레시피 출력</h3>

<h4>레시피 출력 기능은 BardAPI를 이용하여 사용자들에게 여러가지 레시피를 제공합니다.</h4>
<br>
<br>
<br>
<br>
<img src="image/refrigerator.gif"/>
<br>
<br>
<h3>냉장고 - 식품 관리 기능</h3>

<h4>냉장고를 이용하여 식품의 유통기한, 사용처 등을 관리할 수 있습니다.</h4>
<br>
<br>
<br>
<br>
<img src="image/refrigeratorRecipe.gif"/>
<br>
<br>
<h3>냉장고 속 레시피</h3>

<h4>냉장고 속 레시피는 냉장고 안에 저장된 식품으로 만들 수 있는 레시피를 제공합니다.</h4>
<br>
<br>
<br>
<br>

# 사용 기술

- Frontend :
```
 HTML5, CSS3, JavaScript, Jquery
```
- Backend :
```
 Java, Python
```
- Framework :
```
 Spring
```
- Database :
```
 My SQL
```
- Version Control :
```
Git, GitHub
```
# API
<img src="image/Google-Bard-API.jpg" width="200px" height="200px"/>

 <h4>BardAPI</h4>

 <img src="image/Google Custom Search API.jpg" width="200px" height="200px"/>
 
 <h4>GoogleCustomSearchAPI, GoogleYoutubeAPI</h4>
 
 <h4>NaverLoginAPI, GoogleLoginAPI, KakaoLoginAPI</h4>

<img src="image/coolsms.png" width="200px" height="200px"/>

 <h4>CoolSMSAPI</h4>

# DataBase

<img src="image/TotalDB.png"/>

<img src="image/refDB.png"/>

<img src="image/recipeDB.png"/>

<img src="image/cookDB.png"/>

<img src="image/memberDB.png"/>
