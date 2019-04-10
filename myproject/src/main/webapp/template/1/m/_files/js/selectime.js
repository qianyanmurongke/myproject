var currDT=new Date(); ;
function adddate(){
	//向右跳转时间（加时间）的按钮
	//var s = document.getElementById("beginTime").innerHTML;
	var s = $("#daytime").data("datetime");
	var arr = s.split("-"); //将获取的数组按“/”拆分成字符串数组
	
	var year = parseInt(arr[0]);//开分字符串数组的第一个地址的内容是年份
	var month = parseInt(arr[1]);//开分字符串数组的第二个地址的内容是月份
	var date = parseInt( arr[arr.length-1]);//开分字符串数组的第三个地址的内容是日期
	
	if(date == 28){//当日期为28号时 只判断是否是2月
		switch(month)
		{
			  case 2:
				  if(year % 4 == 0 && year % 100 !=0 || year%400 ==0){
					  date = date +1;
					  break;
					  //如果是闰年2月 日期就加一
					 } else{
						  date = 1;
					     month = month +1; 
					     break;
					 //不是闰年2月 日期就变为1 月份加一
						 }
			  default:
					date = date +1;
					break;
						 //其他月份默认日期加一
		}
		
	}else if(date == 29){ //当日期为29号是 也是判断是否是2月
		switch(month)
		{
			  case 2:
				   date = 1;
				   month = month +1;
				   break;
			  default:
				   date = date +1;
				   break;
		} //当29号出现必定是闰年 日期变为1 月份加一 其他月份默认日期加一
		
	}else if(date == 30){ //当日期为30 时
			 switch(month)
			   {
					case 3:
					case 5:
					case 7:
					case 8:
					case 10:
					case 12: 
						 date = date +1;
						 break; //这些月份的时候一个月有31天 到30的时候再加一
					case 4:
					case 6:
					case 9:
					case 11: 
						 date = 1;
						 month = month +1;
						break; //这些月份的时候一个月有30天 到30的时候 日期变为1 月份加一
					
			   }
	   }else if(date == 31){
		   
			 switch(month)
			   {
					case 1:
					case 3:
					case 5:
					case 7:
					case 8:
					case 10:
						 date = 1;
						 month = month+1;
						 break; //这些月份的时候一个月有31天 到31的时候  日期为1月份加一
					case 12: 
						 date = 1;
						 month = 1;
						 year = year+1;;
						 break;  //十二月 的 31 号 日期变为一 月份变为一 年份加一
										
			   }
	   }else{
		   date +=1;
		   }
	
	if (month >= 1 && month <= 9) {
        month = "0" + month;
    }
    if (date >= 0 && date <= 9) {
        date = "0" + date;
    }
	$("#daytime").html(year+"-"+month+"-"+date);
	$("#daytime").data("datetime",year+"-"+month+"-"+date);
	getdate();
 }
	
	
function reducedate(){
	//向左跳转时间（减时间）的按钮
	//var s = document.getElementById("beginTime").innerHTML;
	var s = $("#daytime").data("datetime");
	var arr = s.split("-"); //将获取的数组按“/”拆分成字符串数组
	
	var year = parseInt(arr[0]);//开分字符串数组的第一个地址的内容是年份
	var month = parseInt(arr[1]);//开分字符串数组的第二个地址的内容是月份
	var date = parseInt( arr[arr.length-1]);//开分字符串数组的第三个地址的内容是日期
	
	if(date == 1){//当日期为1时，再剪就会改变月份，甚至年份
		switch(month){
			case 1:
			     date = 31;
				 month = 12;
				 year = year-1;
				 break;  //一月一日 再剪一天 年份减一 月份为12 日期为31
		    case 2:
			case 4:
			case 6:
			case 8:
			case 9:
			case 11:
			     date = 31;
				 month = month-1;
				 break; //这些月一日 再剪一天  月份减一 日期为31
			case 3:
			      if(year % 4 == 0 && year % 100 !=0 || year%400 ==0){
					  date = 29;
					  month = month -1;
					 }else {
						 date = 28;
						 month = month -1;
						 }
			       break; //三月一日 再剪一天  月份减一 日期为根据是否是闰年来判断 日期
			case 5:
			case 7:
			case 10:
			     date = 30;
				 month = month -1;
			     break; //这些月一日 再剪一天  月份减一 日期为30
			}
		}else{
			date = date-1;
			}
	
	if (month >= 1 && month <= 9) {
        month = "0" + month;
    }
    if (date >= 0 && date <= 9) {
        date = "0" + date;
    }
	$("#daytime").html(year+"-"+month+"-"+date);
	$("#daytime").data("datetime",year+"-"+month+"-"+date);
	getdate();
}
//上一周/下一周
function reduceweek(ope){
	var weekStartDate,weekEndDate;
	var num = 0;
	if(ope=="-") {
	   num = -7;
	}
	else if(ope=="+") {
	   num = 7;
	}
	var sstart = $("#weektime").data("weekstartdate");
	var arrstart = sstart.split("-"); //将获取的数组按“/”拆分成字符串数组
	var yearstart = parseInt(arrstart[0]);//开分字符串数组的第一个地址的内容是年份
	var monthstart = parseInt(arrstart[1]);//开分字符串数组的第二个地址的内容是月份
	var datestart = parseInt( arrstart[arrstart.length-1]);//开分字符串数组的第三个地址的内容是日期
	var laststartDay = getLastDay(yearstart,monthstart); 
	datestart+=num;
	if(datestart<1) {
	   monthstart--;
	   if(monthstart<1) {
	    yearstart--;
	    monthstart = 12;
	   }
	   datestart = getLastDay(yearstart,monthstart);
	   datestart=datestart+parseInt( arrstart[arrstart.length-1])+num;
	}
	else if(datestart>laststartDay) {
	   monthstart++;
	   if(monthstart>12) {
	    yearstart++;
	    monthstart = 1;
	   }
	   datestart=num-laststartDay+parseInt( arrstart[arrstart.length-1]);
	}
	if (monthstart >= 1 && monthstart <= 9) {
        monthstart = "0" + monthstart;
    }
    if (datestart >= 0 && datestart <= 9) {
        datestart = "0" + datestart;
    }
    weekStartDate=yearstart+"-"+monthstart+"-"+datestart;
    
	var send = $("#weektime").data("weekenddate");
	var arrend = send.split("-"); //将获取的数组按“/”拆分成字符串数组
	var yearend = parseInt(arrend[0]);//开分字符串数组的第一个地址的内容是年份
	var monthend = parseInt(arrend[1]);//开分字符串数组的第二个地址的内容是月份
	var dateend = parseInt( arrend[arrend.length-1]);//开分字符串数组的第三个地址的内容是日期
    var lastendDay = getLastDay(yearend,monthend); 
	dateend+=num;
	if(dateend<1) {
	   monthend--;
	   if(monthend<1) {
	    yearend--;
	    monthend = 12;
	   }
	   dateend = getLastDay(yearend,monthend);
	   dateend=dateend+parseInt( arrend[arrend.length-1])+num;
	}
	else if(dateend>lastendDay) {
	   monthend++;
	   if(monthend>12) {
	    yearend++;
	    monthend = 1;
	   }
	   dateend=num-lastendDay+parseInt( arrend[arrend.length-1]);
	}
	if (monthend >= 1 && monthend <= 9) {
        monthend = "0" + monthend;
    }
    if (dateend >= 0 && dateend <= 9) {
        dateend = "0" + dateend;
    }
    weekEndDate=yearend+"-"+monthend+"-"+dateend;

	$("#weektime").html(weekStartDate+"~"+weekEndDate);
    $("#weektime").data("weekstartdate",weekStartDate);
    $("#weektime").data("weekenddate",weekEndDate);
    getweek();
}
function reducemonth(ope){
	var s = $("#monthtime").data("monthstartdate");
	var arr = s.split("-"); //将获取的数组按“/”拆分成字符串数组
	var year = parseInt(arr[0]);//开分字符串数组的第一个地址的内容是年份
	var month = parseInt(arr[1]);//开分字符串数组的第二个地址的内容是月份
	var date = parseInt( arr[arr.length-1]);//开分字符串数组的第三个地址的内容是日期
    if(ope == "-") {
    	month--;
    	if(month < 1) {
    		year--;
    		month = 12;
    	}
    }
	else if(ope == "+") {
		month++;
		if(month > 12) {
			year++;
			month = 1;
		}
	}
	date=getLastDay(year,month);
	if (month >= 1 && month <= 9) {
        month = "0" + month;
    }
    if (date >= 0 && date <= 9) {
        date = "0" + date;
    }
	
    var monthstartdate = year+"-"+month+"-01"; 
    var monthenddate = year+"-"+month+"-"+date;
    $("#monthtime").html(year+"年"+month+"月");
    $("#monthtime").data("monthstartdate",monthstartdate);
    $("#monthtime").data("monthenddate",monthenddate);
    getmonth();
}
function getLastDay(y, m) {
	var lastDay = 28;
	if(m == 1 || m == 3 || m == 5 || m == 7 || m == 8 || m == 10 || m == 12) {
		lastDay = 31;
	} else if(m == 4 || m == 6 || m == 9 || m == 11) {
		lastDay = 30;
	} else if(isLeapYear(y) == true) {
		lastDay = 29;
	}
	return lastDay;
}
function isLeapYear(y) {
	var isLeap = false;
	if(y % 4 == 0 && y % 100 != 0 || y % 400 == 0) {
		isLeap = true;
	}
	return isLeap;
}
function startdate(){
	var date = new Date();
    var year=date.getFullYear();
	var month = date.getMonth() + 1;
	var nowDayOfWeek = date.getDay(); 
    var dateStart=new Date(year,month,strDate-nowDayOfWeek+1);
    var dateEnd=new Date(year,month,strDate-nowDayOfWeek+7);
    var monthStart=dateStart.getMonth() ;
    var dayStart=dateStart.getDate();

	if(monthStart >= 1 && monthStart <= 9) {
		monthStart = "0" + monthStart;
	}
	if(dayStart >= 1 && dayStart <= 9) {
		dayStart = "0" + dayStart;
	}
	
	return year+"-"+monthStart+"-"+dayStart;	
}
function enddate(){
	var date = new Date();
    var year=date.getFullYear();
	var month = date.getMonth() + 1;
	var nowDayOfWeek = date.getDay(); 
    var dateStart=new Date(year,month,strDate-nowDayOfWeek+1);
    var dateEnd=new Date(year,month,strDate-nowDayOfWeek+7);
    var monthEnd=dateEnd.getMonth();
    var dayEnd=dateEnd.getDate();
	if(monthEnd >= 1 && monthEnd <= 9) {
		monthEnd = "0" + monthEnd;
	}
	if(dayEnd >= 0 && dayEnd <= 9) {
		dayEnd = "0" + dayEnd;
	}

	return year+"-"+monthEnd+"-"+dayEnd;
}

