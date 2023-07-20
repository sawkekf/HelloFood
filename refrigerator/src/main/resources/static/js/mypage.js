function toggle_menu(){
    const submenu = document.getElementById("myMenu");
    if(submenu.style.display == 'none'){
        submenu.style.display = 'block';
    }
    else{
        submenu.style.display = 'none';
    }
};
function changeFirst(){
	if($('#profileImage').attr("src") == "/img/cook.jpeg"){
		alert("기본 이미지 입니다.");
	}
	else{
		var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");
		$.ajax({
			url: "/member/changFirstImg",
			type: "post",
			beforeSend: function (xhr) {
				xhr.setRequestHeader(header, token);
			},
			success: function (result) {
				alert('프로필 이미지가 변경되었습니다.')
				location.href='/mypage/profile';
			},error: function (error){
				console.log(error);
			}

		});
	}

}


	function bannedAndDelete(t){
		var member = $('.'+t+'member').text();
		var title = $('.'+t+'title').attr('value');
		var content = $('.'+t+'notifiedContent').attr('value');
		var notifiedMember = $('.'+t+'notifiedMember').text();
		var notifiedCase = $('.'+t+'notifiedCase').text();
		var notifiedReason = $('.'+t+'notifiedReason').text();
		var writingId = $('.'+t+'writingId').text();
		var commentId = $('.'+t+'commentId').text();
		var boardId = $('.'+t+'boardId').text();
		console.log(writingId);
	
		window.open('','checkNotified',"toolbar=no,directories=no,scrollbars=no,resizable=no,status=no,menubar=no,width=900, height=700, top=0,left=20")
		$('#member').val(member);
		$('#notifiedMember').val(notifiedMember);
		$('#title').val(title);
		$('#content').val(content);
		$('#notifiedCase').val(notifiedCase);
		$('#notifiedReason').val(notifiedReason);
		$('#writingId').val(writingId);
		$('#commentId').val(commentId);
		$('#boardId').val(boardId);
		$('#notifying').submit();
		
	}
