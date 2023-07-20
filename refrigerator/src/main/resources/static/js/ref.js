
var ref_btn =  document.getElementById("myBtn");
var modal = document.getElementById("overlay");
var close = document.getElementsByClassName("close")[0];
var close2 = document.getElementsByClassName("close2")[0];
var close3 = document.getElementsByClassName("close3")[0];
var close4 = document.getElementsByClassName("close4")[0];
var close5 = document.getElementsByClassName("close5")[0];
var modal2 =  document.querySelector('#modal2');
var overlay2 =  document.querySelector('#overlay2');
var overlay4 =  document.querySelector('#overlay4');
var Ing_saveIng = document.querySelector('.Ing_saveIng');
var overlay3 =  document.querySelector('#overlay3');
var tag = document.getElementById("tag");
let id = document.getElementById('ing_id');
let amount = document.querySelector('#amount');
let ing_deadline = document.querySelector('#ing_deadline');
let ing_insert =  document.querySelector('.ing_inserting');
let ing_selectTag = document.querySelector('.ing_selectTag');
let ing_selectStr = document.querySelector('.ing_selectStr')
let smallTag_placestr = document.querySelector('.smallTag_placestr');
let Ing_delete = document.querySelector('.Ing_delete');
var isDragging = false;
let selList = new Array();
let Ing_deleteIng = document.querySelector('.Ing_deleteIng');
let Ing_mysel = document.querySelector('.Ing_mysel');
let ing_span1 = document.querySelectorAll('.ing_span1');
let ing_div3 = document.querySelector('.ing_div3');
let ing_div2 = document.querySelector('.ing_div2');
let ing_div4 = document.querySelector('.ing_div4');
let modalcontent = document.querySelector('.modal_content');
let refSle = document.querySelector('.refSle');
let addInglist =  new Array();
let ing_content4 = document.querySelector('.ing_content4');
let click = false;
let selectList = new Array();
let overlay5 = document.querySelector('#overlay5');
let today = new Date();
let year = today.getFullYear(); // 년도
let month = today.getMonth() + 1;  // 월
let date = today.getDate();  // 날짜
let day = today.getDay();  // 요일
let colorvalues;

function selectListSave(){
    let contentChild = ing_content4.children;
    for(let i = 0;i < contentChild.length;i++){
        let date =  contentChild[i].children[2].value;
        let memo =  contentChild[i].children[3].value;
        let id =  contentChild[i].children[0].value;
        var token = $("meta[name='_csrf']").attr("content");
        var header = $("meta[name='_csrf_header']").attr("content");
        let param = {
            ingId: id,
            daeLine: new Date(date),
            memo: memo
        };
        $.ajax({
            url : "/list/detailSave",
            type : "POST",
            contentType : "application/json; charset=UTF-8",
            data : JSON.stringify(param),
            beforeSend: function (xhr) {
                xhr.setRequestHeader(header, token);
            },
            success  : function(data){
                if ($('div.my_ref_style')[0]) {
                    firstStartIng();
                    firstLoading();
                    startClick();

                }
                modal4hide();
                ref_btn.click();
            }

            ,
            error : function(request, status, error) {
                console.log(error);
            }
        });

    }


}
function listDetail(){
    console.log(selList);
    if(selList.length == 0){
        alert("선택해주세요");
    }
    else{
        var token = $("meta[name='_csrf']").attr("content");
        var header = $("meta[name='_csrf_header']").attr("content");
        $.ajax({
            url : "/list/detail",
            type : "POST",
            contentType : "application/json; charset=UTF-8",
            data : JSON.stringify(selList),
            beforeSend: function (xhr) {
                xhr.setRequestHeader(header, token);
            },
            success  : function(data){
                console.log(data);
                modal4show();
                modalContent4(data);
            }
            ,error : function(request, status, error) {
                console.log(error);
            }
        });
    }
}
function modalContent4(data){
    let list1 = new Array();
    let list2 = new Array();
    for(let i=0;i<data.length;i++){
        if(data[i].ing_deadline != null){
            list1.push(data[i]);
        }
        else{
            list2.push(data[i]);
        }
    }
    let orderedDate = list1.sort((a, b) => new Date(a.ing_deadline) - new Date(b.ing_deadline))
    for(let i =0;i<list2.length;i++){
        list1.push(list2[i]);
    }


    ing_content4.innerHTML = listHtml(list1);

}

function listHtml(data){
    let datalist = [];
    let str = "";
    let dataes = "";
    data.forEach((fruit,idx) => {
        if(fruit.ing_deadline != null){
            let  str1 =  fruit.ing_deadline.split('T');
            dataes= str1[0];
        }
        else{
            dataes = "";
        }
        datalist.push(`
           <div style="margin: 2%;">
            <button value = ${fruit.id} style=" margin-left: 6%; background: none; border: none;">
                    <img src = ${fruit.srcs} width="50px" height="50px" style="border-radius: 500px; border: solid 1px;">
                </button>

                <span class="textfamily" style=" width: 93px;  display: inline-block; position: relative; top: -20px;">${fruit.smallTags}</span>
                <input type="date" value = "${dataes}" style=" padding: 8px;border-radius: 5px; border: 1px solid #ccc; background-color: #f2f2f2; font-size: 16px; height: 14px; width: 125px; margin-right: 2%; top: -20px; position: relative;">
                <input type="textarea" value="${fruit.memo}" style=" padding: 8px; border-radius: 5px; border: 1px solid #ccc; background-color: #f2f2f2; font-size: 16px; height: 14px; width: 30%; position: relative; top: -20px;">

                <input type="hidden" class ="backgrounds" value = "${fruit.ing_deadline}" >
                 </div>
                 `);

    });


    datalist.forEach((element) => {
        const separator = " ";
        if (str.length == 0) {
            str = str + element;
        } else {
            str = str + separator + element;
        }
    })



    return str;

}

refSle.oninput = function(){
    let text =   refSle.value;
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    var url = "/main/selectIng";
    if(text != ""){
        $.ajax({
            url : url,
            type : "POST",
            contentType : "application/json; charset=UTF-8",
            data : text,
            beforeSend: function (xhr) {
                xhr.setRequestHeader(header, token);
            },
            success  : function(data){
                console.log(data)
                $('.selectSpanBig').css('color','black');
                modalcontent.innerHTML="";
                arr2lists2(data);
                dbclick();
                backgrounds();
                start();
            }
            ,error : function(request, status, error) {
                console.log(error);
            }
        });
    }
    else{
        ref_btn.click();
    }


}

function backgrounds(){



    console.log(day);
    let btns = document.querySelectorAll('.ing_click-btn');
    for(let i =0;i<btns.length;i++){
        dateValue = btns[i].nextElementSibling.nextElementSibling.value;
        if(dateValue != "null"){
            let str =  dateValue.split('T');
            let myear = str[0].split('-')[0] - year;
            let mmonth = str[0].split('-')[1] - month;
            let mday = str[0].split('-')[2] - date;
            console.log(myear);
            console.log(mmonth);
            console.log(mday);
            if(myear >0 ){         }
            else if(mmonth>0){ }
            else if(myear == 0 && mmonth ==0){
                if(mday>1)
                {

                }
                else if(0<=mday && mday<=1){
                   console.log(btns[i].nextElementSibling.lastElementChild.previousElementSibling.lastElementChild.textContent);
                    btns[i].nextElementSibling.lastElementChild.previousElementSibling.lastElementChild.textContent = str[0].split('-')[0]+"-"+str[0].split('-')[1]+"-"+str[0].split('-')[2] ;
                    var p = document.createElement("span");
                    p.className = "dangerspan";
                    btns[i].nextElementSibling.lastElementChild.previousElementSibling.lastElementChild.appendChild(p);
                    p.innerHTML=" (임박)";
                    btns[i].nextElementSibling.lastElementChild.previousElementSibling.previousElementSibling.previousElementSibling.style.backgroundColor='white';
                    btns[i].nextElementSibling.firstElementChild.style.color = 'red';
                    btns[i].nextElementSibling.firstElementChild.style.textDecoration = 'line-through';
                    btns[i].nextElementSibling.lastElementChild.previousElementSibling.firstElementChild.style.color = 'red';
                    btns[i].nextElementSibling.lastElementChild.previousElementSibling.lastElementChild.style.color = 'red';
                    btns[i].style.backgroundColor = 'red';
                    btns[i].classList.add('blink');

                }
                else{

                    btns[i].style.border = '1px solid black';
                    btns[i].style.backgroundColor = 'gray';
                }
            }
            else{

                btns[i].style.backgroundColor = 'black';
            }
        }
    }

}




for(let i = 0;i < ing_span1.length;i++){
    ing_span1[i].addEventListener('click',function(){

        var url = "/ingredient/mySel";
        let text = this.innerHTML;
        var token = $("meta[name='_csrf']").attr("content");
        var header = $("meta[name='_csrf_header']").attr("content");

        $.ajax({
            url : url,
            type : "POST",
            contentType : "application/json; charset=UTF-8",
            data : text,
            beforeSend: function (xhr) {
                xhr.setRequestHeader(header, token);
            },
            success  : function(data){

                insertis(data);

                divshow4();
                inse();

            }
            ,error : function(request, status, error) {
                console.log(error);
            }
        });

    });
}
function clicksave(){
    let text =  document.querySelector('#ing_selectTag').value;
    let datae = document.querySelector('#ing_deaLine2').value;
    let memo = document.querySelector('.textAreass').value;
    text = text.trim();
    if(text != ""){
        let param = {
            text:text,
            datae: new Date(datae),
            memo:memo
        }
        addInglist.push(param);
        console.log(addInglist);

        createHtml(addInglist);

        document.querySelector('#ing_deaLine2').value= "";
        document.querySelector('.textAreass').value="";
        document.querySelector('#ing_selectTag').value= "" ;
        $('.ing_div3').css('visibility','hidden');
        $('.ing_div3').css('opacity','0');
        $('.ing_div2').css('visibility','hidden');
        $('.ing_div2').css('opacity','0');
        $('.ing_div4').css('visibility','hidden');
        $('.ing_div4').css('opacity','0');
    }
    else{
        alert("재료를 선택하지 않았습니다");
    }

}
function createHtml(data){
    document.querySelector('.ingListPlace').innerHTML="";
    for(let i = 0;i < data.length;i++){

        createhtml2(data[i],i) ;

    }
    listDel();
}
function inse(){
    let ingli = document.querySelectorAll('.ingli');
    for(let i = 0;i < ingli.length;i++){
        ingli[i].addEventListener('click',function(){

            document.querySelector('#ing_selectTag').value = this.innerHTML;

        });
    }
}
document.querySelector('.createTag').onclick = function (){
    overlay5Show();

}

Ing_mysel.onclick= function(){
    $('.ing_selectTag').val("");
    $('.smallTag_placestr').html("");

    if(ing_div2.style.visibility == "hidden"){
        ing_div3.style.visibility = "visible";
        ing_div3.style.opacity = 1;
        ing_div2.style.visibility = "visible";
        ing_div2.style.opacity = 1;
        divhide4();
        divshow2();
    }
    else {
        ing_div2.style.visibility = "hidden";
        ing_div2.style.opacity = 0;
        divhide4();
        divshow2();
    }

}
function overlay3(){
    divhide3();
    divhide4();
}
function divshow3(){
    ing_div3.style.visibility = "visible";
    ing_div3.style.opacity = 1;

}
function divhide3(){
    ing_div3.style.visibility = "hidden";
    ing_div3.style.opacity = 0;

}
function divshow4(){
    ing_div4.style.visibility = "visible";
    ing_div4.style.opacity = 1;

}
function divhide4(){
    ing_div4.style.visibility = "hidden";
    ing_div4.style.opacity = 0;

}
function divshow2(){
    ing_div2.style.visibility = "visible";
    ing_div2.style.opacity = 1;

}
function divhide2(){
    ing_div2.style.visibility = "hidden";
    ing_div2.style.opacity = 0;

}
Ing_deleteIng.onclick = function(){
    var url = "/ingredient/deletes";
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    if(selList.length !=0){
        $.ajax({
            url : url,
            type : "POST",
            contentType : "application/json; charset=UTF-8",
            data :  JSON.stringify(selList),
            beforeSend: function (xhr) {
                xhr.setRequestHeader(header, token);
            },
            success  : function(data){

                alert("성공");
                if ($('div.my_ref_style')[0]) {

                    firstStartIng();
                    firstLoading();
                    startClick();
                }
            }
            ,error : function(request, status, error) {
                console.log(error);
            },
            complete: function() {
                modal3hide();
                ref_btn.click();
            }
        });
    }
    else{
        alert("실패");
    }
}
function createhtml23(data){
    let datalist = [];

    data.forEach((fruit) => {
        datalist.push(`
               <div style="display: inline-block;">
                        <button class = "rec_ing" value = "${fruit.id}" style="display: block; border-radius: 500px;  border: solid 1px;">
                            <img src = "${fruit.srcs} "width="20px" height="27px">

                        </button>
                        <span >${fruit.smallTags}</span>
                    </div>
                `);
    });




    let str = "";

    datalist.forEach((element) => {
        const separator = " ";
        if (str.length == 0) {
            str = str + element;
        } else {
            str = str + separator + element;
        }
    })
    return str;
}

document.querySelector('.ing_selectAll').onclick = function(){
    let btnss = document.querySelectorAll('.ing_click-btn');
    for(let i = 0;i < btnss.length;i++){
        btnss[i].click();
    }



}
function checkSelectALl(){
    let btnss = document.querySelectorAll('.ing_click-btn');

    if(selList.length != btnss.length){
        $('.ing_selectAll').css('color','black');
    }
    else{
        $('.ing_selectAll').css('color','red');
    }
}



ing_insert.onclick= function(){
    show3();
    ing_selectStr.style.visibility = "visible";
}

function start(){
    let btnss = document.querySelectorAll('.ing_click-btn');
    for(let i = 0;i < btnss.length;i++){
        btnss[i].addEventListener('click',function(){
            selectda(this)
        });
    }
}
function selectda(thiss){
    console.log(thiss.value);
	var deadLineCheck = thiss.parentNode.lastElementChild.previousElementSibling.lastElementChild.previousElementSibling.lastElementChild.innerText;
	var outLine = thiss.parentNode
    let deadLineCheck2 = deadLineCheck;
	deadLineCheck = deadLineCheck.indexOf('종료');
	console.log(outLine);
    if(selList.indexOf(thiss.value) != -1){
        selList =  selList.filter((ele)=>  ele != thiss.value
        )
        if(deadLineCheck==-1){
	        thiss.style.border="1px solid black";
	        thiss.style.backgroundColor='white';
	        outLine.style.border="none";
	        outLine.style.backgroundColor="white";
		}else{
			thiss.style.border="1px solid black";
	        thiss.style.backgroundColor='gray';
	        outLine.style.border="none";
	        outLine.style.backgroundColor="white";
		}
    }
    else{
        selList.push(thiss.value);
        if( deadLineCheck2.indexOf('종료')!=-1){

            thiss.style.border="1px solid black";
            thiss.style.backgroundColor="gray";
            outLine.style.border="1px dashed black";
            outLine.style.borderRadius="3px";
            outLine.style.backgroundColor="lightgray"

		}
        else if(deadLineCheck2.indexOf('임박')!=-1){
            thiss.style.border="3px solid red";
            thiss.style.backgroundColor="white";
            outLine.style.border="1px dashed red";
            outLine.style.borderRadius="3px";
            outLine.style.backgroundColor="coral"
        }
        else{
            thiss.style.border="3px solid green";
            thiss.style.backgroundColor="white";
            outLine.style.border="1px dashed green";
            outLine.style.borderRadius="3px";
            outLine.style.backgroundColor="lightgreen"
		}
    }
    checkSelectALl();
    console.log(selList);
}

Ing_delete.onclick = function(){

    var url = "/ingredient/delete";
    let deleteId = id.value;
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    $.ajax({
        url : url,
        type : "POST",
        contentType : "application/json; charset=UTF-8",
        data : deleteId,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success  : function(data){
            if ($('div.my_ref_style')[0]) {
                firstStartIng();
                firstLoading();
                startClick();
            }
            modal2hide();
            ref_btn.click();
        }
        ,error : function(request, status, error) {
            console.log(error);
        }
    });
}


ing_selectTag.oninput  = function(){
    var url = "/ingredient/create";
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    let tagName = ing_selectTag.value;
    if(tagName != ""){

        $.ajax({
            url : url,
            type : "POST",
            contentType : "application/json; charset=UTF-8",
            data : tagName,
            dataType : 'json',

            beforeSend: function (xhr) {
                xhr.setRequestHeader(header, token);
            },
            success  : function(data){
                inserti(data);

                sow();
            }
            ,error : function(request, status, error) {
                console.log(error);
            }
        });
    }

}


Ing_saveIng.onclick = function(){
    var url = "/ingredient/detail";
    let data = document.querySelector('#regTime').value;

    let texts = document.querySelector('.textAreas').value;
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    var paramData = {
        id:  parseInt(id.value),
        ing_deadline: new Date(ing_deadline.value),
        smallTags: tag.value,
        texts : texts,
        regTime : new Date(data)
    };
    var param = JSON.stringify(paramData);
    $.ajax({
        url : url,
        type : "POST",
        contentType : "application/json; charset=UTF-8",
        data : param,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success  : function(){
            if ($('div.my_ref_style')[0]) { //특정 페이지에서 사용할 시
                firstStartIng(); //냉장고 속
                firstLoading();
                startClick();

            }
            modal2hide();
            ref_btn.click();
        },
        error : function(request, status, error) {
            console.log(error);
        }
    });

}
function sow(){
    let insertIng = document.querySelectorAll('.ing_spro');

    for(let i = 0;i < insertIng.length;i++){
        insertIng[i].addEventListener('click',function(){
            document.querySelector('#ing_selectTag').value  =  this.value;


        });
    }
}


ref_btn.onclick = function(){
    let url = "/main/ingList";
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    $.ajax({
        url : url,
        type : "GET",
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success  : function(data){
            modalcontent.innerHTML = "";
            showAllList(data); // 전달 받은 데이터로 구조 및 아이템 생성
            show2(); //모달 창 보이게 style 변경
            dbclick(); //아이템에 더블 클릭 시 모달 창 보여주기 및
            start(); //전체 클릭 시 각각 아이템에 클릭 이벤트 발생
            select1();//분류하는 span에 click이벤트 생성
            selList = new Array();
            $('.selectSpanBig').css('color','black');
            $('.ing_selectAll').css('color','black');

            backgrounds(); //유통기간 남은 날짜 별 색깔 표시
        }
    });

}



ing_selectTag.addEventListener('focus', () => {
    ing_selectStr.style.visibility = "hidden";
    ing_div3.style.visibility = "hidden";
    ing_div3.style.opacity = 0;
    ing_div2.style.visibility = "hidden";
    ing_div2.style.opacity = 0;
    ing_div4.style.visibility = "hidden";
    ing_div4.style.opacity = 0;

});
ing_selectTag.addEventListener('blur', () => {

    ing_selectStr.style.visibility = "visible";

});

function showAllList(data){
    arr2lists(data);

}
function arr2lists(arr1){

    if(arr1.length != 0){
        var div = document.createElement('div');
        var div2 = document.createElement('div');
        div.setAttribute('class','ing_sel2');
        div2.setAttribute('class','ing_sel3');
        div2.innerHTML= sdada(arr1);
        div.appendChild(div2);
        modalcontent.appendChild(div);
    }
    else{
        var div = document.createElement('div');
        var div2 = document.createElement('div');
        div.setAttribute('class','ing_sel2');
        div2.setAttribute('class','ing_sel3');

        div.appendChild(div2);
        modalcontent.appendChild(div);
    }
}
function arr2lists2(arr1){


    var div = document.createElement('div');
    var div2 = document.createElement('div');
    div.setAttribute('class','ing_sel2');
    div2.setAttribute('class','ing_sel3');
    div2.innerHTML= sdada(arr1);
    div.appendChild(div2);
    modalcontent.appendChild(div);

}
function select1(){

    let spans = document.querySelectorAll('.selectSpanBig');
    for(let i = 0;i < spans.length;i++){
        spans[i].addEventListener('click',function(){
            selList = new Array();
            $('.selectSpanBig').css('color','black');
            $(this).css('color','red');
            console.log(this);
            let value = this.innerHTML;
                console.log(colorvalues);
                if (value == "전체") {
                    value = 0;
                    if(colorvalues == value){
                        console.log(colorvalues);

                        value = 0;
                        $(this).css('color','black');
                    }
                    else{
                        colorvalues = 0;
                    }
                } else if (value == "채소") {
                    value = 1;
                    if(colorvalues == value){
                        console.log(colorvalues);

                        value = 0;
                        colorvalues = 0;
                        $(this).css('color','black');
                    }
                    else{
                        colorvalues = 1;
                    }
                } else if (value == "육류") {
                    value = 2;
                    if(colorvalues == value){
                        console.log(colorvalues);

                        value = 0;
                        colorvalues = 0;
                        $(this).css('color','black');
                    }
                    else{
                        colorvalues = 2;
                    }
                } else if (value == "해산물") {
                    value = 3;
                    if(colorvalues == value){
                        console.log(colorvalues);

                        value = 0;
                        colorvalues = 0;
                        $(this).css('color','black');
                    }
                    else{
                        colorvalues = 3;
                    }
                } else if (value == "유제품") {
                    value = 4;
                    if(colorvalues == value){
                        console.log(colorvalues);

                        value = 0;
                        colorvalues = 0;
                        $(this).css('color','black');
                    }
                    else{
                        colorvalues = 4;
                    }
                } else if (value == "과일") {
                    value = 5
                    if(colorvalues == value){
                        console.log(colorvalues);

                        value = 0;
                        colorvalues = 0;
                        $(this).css('color','black');
                    }
                    else{
                        colorvalues = 5;
                    }

                } else if (value == "임박") {
                    value = 7;
                    if(colorvalues == value){
                        console.log(colorvalues);

                        value = 0;
                        colorvalues = 0;
                        $(this).css('color','black');
                    }
                    else{
                        colorvalues = 7;
                    }
                } else {
                    value = 6;
                    if(colorvalues == value){
                        console.log(colorvalues);

                        value = 0;
                        colorvalues = 0;
                        $(this).css('color','black');
                    }
                    else{
                        colorvalues = 6;
                    }
                }

            let url = "/main/ingList";
                var token = $("meta[name='_csrf']").attr("content");
                var header = $("meta[name='_csrf_header']").attr("content");
                $.ajax({
                    url: url,
                    type: "GET",
                    beforeSend: function (xhr) {
                        xhr.setRequestHeader(header, token);
                    },
                    success: function (data) {
                        chooseSelect(data, value);
                    }
                    , error: function (request, status, error) {
                        console.log(error);
                    }

                });

        });
    }
}
function chooseSelect(data,value){
    modalcontent.innerHTML = "";
    console.log(data);
    let arr1 = new Array();
    if(value != 7){
        if(value !=0){
            for(let i =0; i<data.length;i++){
                if(data[i].big == value){
                    arr1.push(data[i]);
                }
            }
            showAllList(arr1);
        }
        else{
            showAllList(data);
        }
    }else{

        for(let i =0; i<data.length;i++){

            if(data[i].ing_deadline != null){
                let str =  data[i].ing_deadline.split('T');
                let myear = str[0].split('-')[0] - year;
                let mmonth = str[0].split('-')[1] - month;
                let mday = str[0].split('-')[2] - date;
                if(myear >0 ){         }
                else if(mmonth>0){ }
                else if(myear == 0 && mmonth ==0){
                    if(mday>1) {      }
                    else if(0<=mday && mday<=1){
                        arr1.push(data[i]);
                    }
                    else{
                    }
                }
                else{
                }
            }
            else{

            }



        }
        console.log(arr1);
        showAllList(arr1);
    }

    dbclick();
    start();
    backgrounds();

}




document.querySelector('.ing_Insertlist').onclick = function(){

    url = "/select/save";
    let param = JSON.stringify(addInglist);
    console.log(param);
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    $.ajax({
        url : url,
        type : "POST",
        contentType : "application/json; charset=UTF-8",

        data : param,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success  : function(data){
            console.log(data);
            if ($('div.my_ref_style')[0]) {
                firstStartIng();
                firstLoading();
                startClick();

            }
            modal3hide();
            ref_btn.click();
            alert("재료가 저장되었습니다.");
        }
        ,error : function(request, status, error) {
            console.log(error);
        }
    });
}
function listSort(data){
    let strar = new Array();
    let strar2 = new Array();
    for(let i=0;i<data.length;i++){
        if(data[i].ing_deadline != null){
            strar.push(data[i]);
        }
        else{
            strar2.push(data[i]);
        }
    }
    let orderedDate = strar.sort((a, b) => new Date(a.ing_deadline) - new Date(b.ing_deadline))
    for(let i =0;i<strar2.length;i++){
        strar.push(strar2[i]);
    }
    return strar;
}
function sdada(data){
    let datalist = [];
    let str = "";
    let listdata = listSort(data);
	var now = new Date();
    var now1 = new Date().getDate();


	
    datalist.push(`<div class = "allDiv" id="style-1">`);
    listdata.forEach((fruit,idx) => {

        datalist.push(`
           <div class = "ing_btnList">
            <button class = "ing_click-btn" value = ${fruit.id}>
                    <img src = ${fruit.srcs} width="20px" height="27px">
                </button>
                <div style="display: inline-block;">
                <span class = "ingSpan" >${(fruit.ing_deadline==null)? fruit.smallTags : (now>new Date(fruit.ing_deadline.split('T')[0]))? '<span style=" text-decoration:line-through;">'+fruit.smallTags+'</span>':fruit.smallTags}</span><br>
                <input type="textarea" value="${fruit.memo}" style="  border-radius: 4px;  border: solid 1px;     width: 156px; ${(fruit.ing_deadline==null)?'color:gray;':(now>new Date(fruit.ing_deadline.split('T')[0]))?'color:black; background:gray;':'color:gray;'}" readonly><br>`);

        datalist.push(`
                <span class = "ingDeadLine">${(fruit.ing_deadline==null)?'<span style="font-weight : bolder;">유통기한 : </span> ----':(now>new Date(fruit.ing_deadline.split('T')[0]))? '<span style="font-weight : bolder; color:black;">유통기한 : </span>'+'<span style="color:black;"><span>'+fruit.ing_deadline.split('T')[0]+'(종료)</span></span>':'<span style="font-weight : bolder;">유통기한 : </span><span>'+ fruit.ing_deadline.split('T')[0]}</span></span><br>`);
        datalist.push(`
                </div>
                <input type="hidden" class ="backgrounds" value = ${fruit.ing_deadline}>
                 </div>`);

    });
    datalist.push(`</div>`);
	
	
    datalist.forEach((element) => {
        const separator = " ";
        if (str.length == 0) {
            str = str + element;
        } else {
            str = str + separator + element;
        }
    })
    return str;

}
close.onclick = function(){
    modalcontent.replaceChildren();
    allHide();
}
close2.onclick = function(){
    modal2hide();
}
close3.onclick = function(){
    modal3hide();

}
close4.onclick = function(){
    modal4hide();

}
close5.onclick = function(){
    overlay5hide();

}
window.onclick = function(e){
    if(e.target == modal) {
        allHide();
    }
    if(e.target == overlay2){
        modal2hide();
    }
    if(e.target == overlay3){
        modal3hide();

    }
    if(e.target == overlay4){
        modal4hide();

    }
    if (e.target == overlay5){
        overlay5hide();
    }
}
function dbclick(){
    let btnss = document.querySelectorAll('.ing_click-btn');
    for(let i = 0;i < btnss.length;i++){
        btnss[i].addEventListener('dblclick',function(){
            var url = "/main/ingredient";
            let ids =  this.value;
            var token = $("meta[name='_csrf']").attr("content");
            var header = $("meta[name='_csrf_header']").attr("content");

            $.ajax({
                url : url,
                type : "POST",
                contentType : "application/json; charset=UTF-8",
                data : JSON.stringify(ids),
                dataType : 'json',
                beforeSend: function (xhr) {
                    xhr.setRequestHeader(header, token);
                },
                success  : function(data){
                    if(data.ing_deadline!= null){

                        let str1 =  data.ing_deadline.split('T');

                        ing_deadline.value = str1[0];
                    }
                    else{
                        ing_deadline.value = data.ing_deadline;
                    }
                    if(data.regTime!= null){

                        let str1 =  data.regTime.split('T');

                        document.querySelector('#regTime').value = str1[0];
                    }
                    else{
                        document.querySelector('#regTime').value = data.regTime;
                    }


                    console.log(data);
                    id.value = data.id;
                    tag.value = data.smallTags;

                    document.querySelector('.textAreas').value = data.texts;

                    show();
                }
            });
        });
    }
}
function show(){
    overlay2.style.visibility = "visible";
    overlay2.style.opacity = 1;

}
function show2(){

    modal.style.visibility = "visible";
    modal.style.opacity = 1;
}
function show3(){
    document.querySelector('.ingListPlace').innerHTML="";
    addInglist = new Array();
    overlay3.style.visibility = "visible";
    overlay3.style.opacity = 1;
}
function modal3hide(){
    document.querySelector('.ing_selectTag').value = "";
    document.querySelector('.smallTag_placestr').innerHTML = "";
    overlay3.style.visibility = "hidden";
    overlay3.style.opacity = 0;
    ing_div3.style.visibility = "hidden";
    ing_div3.style.opacity = 0;
    ing_div2.style.visibility = "hidden";
    ing_div2.style.opacity = 0;
    ing_div4.style.visibility = "hidden";
    ing_div4.style.opacity = 0;
    ing_selectStr.style.visibility = "hidden";
}

function modal2hide(){

    overlay2.style.visibility = "hidden";
    overlay2.style.opacity = 0;


}
function modal4hide(){
    overlay4.style.visibility = "hidden";
    overlay4.style.opacity = 0;
}
function modal4show(){

    overlay4.style.visibility = "visible";
    overlay4.style.opacity = 1;
}
function allHide(){
    modalcontent.replaceChildren();
    modal.style.visibility = "hidden";
    modal.style.opacity = 0;

}
function overlay5Show(){
    overlay5.style.visibility = "visible";
    overlay5.style.opacity = 1;
}
function overlay5hide(){
    overlay5.style.visibility = "hidden";
    overlay5.style.opacity = 0;
}

function inserti(data){
    let datalist = [];
    datalist.push('<div style="max-height: 360px;overflow-y: auto; margin-left: 20px;">');

    data.forEach((fruit,idx) => {
        datalist.push(`
                <button class = "ing_spro" id = '${fruit.id}' value = '${fruit.tag_small_name}'>
                    <span>${fruit.tag_small_name}</span>
                </button>
                `);
    });

    datalist.push('</div>');

    smallTag_placestr.innerHTML = datalist;
    let str = "";

    datalist.forEach((element) => {
        const separator = " ";
        if (str.length == 0) {
            str = str + element;
        } else {
            str = str + separator + element;
        }
    })
    smallTag_placestr.innerHTML = str;
}
function dateFormat(date) {
    let dateFormat2 = date.getFullYear() +
        '-' + ( (date.getMonth()+1) < 9 ? "0" + (date.getMonth()+1) : (date.getMonth()+1) )+
        '-' + ( (date.getDate()) < 9 ? "0" + (date.getDate()) : (date.getDate()) );
    return dateFormat2;
}

function insertis(data){
    let datalist = [];


    data.forEach((fruit,idx) => {
        datalist.push(`
                <span class = "ingli" id = ${fruit.id}>${fruit.tag_small_name} </span>
                `);
    });

    ing_div4.innerHTML = datalist;
    let str = "";

    datalist.forEach((element) => {
        const separator = " ";
        if (str.length == 0) {
            str = str + element;
        } else {
            str = str + separator + element;
        }
    })
    ing_div4.innerHTML = str;
}
function createhtml2(data,i){
    let datalist = [];
    let  dataes =  data.datae;
    dataes = dateFormat(dataes)

    datalist.push(`<div class="refListDiv">`);

    datalist.push(`
                <span class="ingName">${data.text} </span>
                <input type="date" value = '${dataes}' class="ingDate" readonly="readonly">
                <input type="text" value = '${data.memo}' class="ingText" readonly="readonly">
                <span class="list-del" style="cursor: pointer;" id = ${i}>×</span>
                `);

    datalist.push(`</div>`);

    let str = "";

    datalist.forEach((element) => {
        const separator = " ";
        if (str.length == 0) {
            str = str + element;
        } else {
            str = str + separator + element;
        }
    })

    document.querySelector('.ingListPlace').innerHTML += str;


}
function listDel(){
    console.log("sda");
    let listDel = document.querySelectorAll('.list-del');
    for(let i = 0;i < listDel.length;i++){
        listDel[i].addEventListener('click',function(){
            console.log(this.id);

            addInglist.splice(this.id,1);

            console.log(addInglist);
            createHtml(addInglist);
        });
    }

}
document.querySelector('.tagsave').onclick = function (){
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    var myselectTag = $('.myselectTag').val();
    let tagname =  $('.tagname').val();
    let param = {
        name: tagname,
        bigTag : myselectTag
    }

    $.ajax({
        url : "/createTag",
        type : "POST",
        contentType : "application/json; charset=UTF-8",
        data :  JSON.stringify(param),
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success  : function(data){

           alert("생성되었습니다");
            close5.click();
        }
        ,error : function(request, status, error) {
            console.log(error);
        }
    });
}

