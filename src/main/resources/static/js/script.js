
const toggleSidebar = () => {
	if ($(".sidebar").is(":visible")) {

		$(".sidebar").css("display", "none");
		$(".content").css("margin-left", "0%");
		console.log("closed");
	}
	else {
		$(".sidebar").css("display", "block");
		$(".content").css("margin-left", "20%");

	}
};

function applyDate(input1) {
	//var input2 = document.getElementsByClassName("dateField");
	/*for (var i = 0; i < input2.length; i++) {
		input2[i].value = input1.value;
	}
	console.log(input1.value);*/
	window.location = "/admin/mark-attendance/?date=" + input1.value;
}

const filter = () => {
	var from = $("#fromdate").val();
	var to = $("#todate").val();
	var emp = $("#emp").val();
	window.location = "/admin/fetchRecords/?from=" + from + "&to=" + to +"&emp=" + emp; 
}
const filterNormal = () => {
	var from = $("#fromdate").val();
	var to = $("#todate").val();
	window.location = "/user/view/?from=" + from + "&to=" + to; 
}
const filterexport = () => {
	var from = $("#fromdate").val();
	var to = $("#todate").val();
	var emp = $("#emp").val();
	window.location = "/admin/downloadRecords/?from=" + from + "&to=" + to +"&emp=" + emp; 
}
