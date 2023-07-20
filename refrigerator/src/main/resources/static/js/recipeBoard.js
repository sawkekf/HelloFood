function goWriting(){
	var member = /*[[${member.name}]]*/'';
	console.log(member);
	if(member=='guest'){
		alert('로그인 후 시도바랍니다.')
		location.href='/member/login';
	}
	if(member!='guest'){
		var bannCheck = /*[[${member.banned}]]*/'';
		var bannDate = /*[[${#temporals.format(member.Banndate,'yyyy-MM-dd HH:mm:ss')}]]*/'';
		console.log(bannCheck);
		if(bannCheck=='Y'){
			alert('차단당하셨습니당. 차단기간은 ['+bannDate+'] 까지입니당')
		}else{			
			//location.href='/recipeBoard/recipeWrite';
		}
	}
}