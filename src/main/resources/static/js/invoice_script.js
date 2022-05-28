
$("#sendOverEmail").change(function() {
    if(this.checked) {
		$("#emailId").removeClass("hidden");
	}else{
		$("#emailId").addClass("hidden");
	}
});

const toggleCustomerForm = () => {
	$("#customerFormDiv").removeClass("hidden");
	$("#itemFormDiv").addClass("hidden");
	$("#invoiceFormDiv").addClass("hidden");
}

const toggleItemForm = () => {
	$("#itemFormDiv").removeClass("hidden");
	$("#customerFormDiv").addClass("hidden");
	$("#invoiceFormDiv").addClass("hidden");
}

const toggleInvoiceForm = () => {
	$("#invoiceFormDiv").removeClass("hidden");
	$("#customerFormDiv").addClass("hidden");
	$("#itemFormDiv").addClass("hidden");
}

const addRows = () => {
	var a = $("#itemEntries");
	var b = $(".itemEntryDiv").clone()[0];
	b.removeAttribute('id')
	//b.appendTo(a);
	//b.children("input.rate").innerHTML = '';
	$(b).children('input.qty').val("");
	$(b).children('input.rate').val("");
	$(b).children('h5.total').text("0");

	a.append(b);
	refreshValues();
}

function removeDiv(l) {
	var a = $(l).closest(".itemEntryDiv");
	if (a.attr('id') != 'first') {
		a.remove();
	}
	refreshValues();
}


function calculateTotal(l) {
	var parentDiv = l.closest(".itemEntryDiv");
	var rate = $(parentDiv).children("input.rate").val();
	var qty = $(parentDiv).children("input.qty").val();
	rate = rate == undefined ? 0 : rate;
	qty = qty == undefined ? 0 : qty;
	var totalEl = $(parentDiv).children("h5.total");
	$(totalEl).text(rate * qty);
	var grand = 0;
	var t;
	var ls = $(".total");
	for (let i = 0; i < ls.length; i++) {
		t = parseInt(ls[i].innerHTML);
		t = Number.isNaN(t) ? 0 : t;
		grand += t;
	}
	$("#grandTotal").text(grand);
}

function refreshValues() {
	var grand = 0;
	var ls = $(".total");
	var t;
	for (let i = 0; i < ls.length; i++) {
		t = parseInt(ls[i].innerHTML);
		t = Number.isNaN(t) ? 0 : t;
		grand += t;
	}
	$("#grandTotal").text(grand);
}
function generateInvoice() {
	var invoice = {}
	var customerId = $("#customerId").val();


	var invNo = $("#invNo").val();
	var date = $("#invoiceDate").val();
	var customer = {};
	customer["custId"] = customerId;
	invoice["customer"] = customer;
	invoice["date"] = date;
	invoice["itemList"] = [];
	invoice["invNo"] = invNo;
	var itemEntries = $(".itemEntryDiv");
	for (let i = 0; i < itemEntries.length; i++) {
		var item = {};
		var select = $(itemEntries[i]).children(".itemEntrySelect")[0];
		var itemId = select.options[select.selectedIndex].value;
		item["itemId"] = itemId;
		if(itemId == ""){
			alert("Please select a Item");
		}
		var rate = $(itemEntries[i]).children("input.rate").val();
		var qty = $(itemEntries[i]).children("input.qty").val();
		rate = rate == "" || rate<0 ? 0:rate;
		qty = qty == "" || qty<0 ? 0:qty;
		var items = {};
		items["item"] = item;
		items["rate"] = rate;
		items["qty"] = qty;
		invoice["itemList"].push(items);
	}

	// validations
	var msg = "";
	if (invNo == "") {
		msg += " Invoice Number"
	}
	if (customerId == "") {
		msg += " Customer";
	}
	if (date == "") {
		msg += " Date";
	}
	console.log("msg = " + msg);
	if (msg != "") {
		console.log("its a trie");
		alert("Please select" + msg);
	} else {
		var url = "/admin/invoice/createone";
		$.ajax({
			type: "POST",
			contentType: "application/json",
			url: url,
			data: JSON.stringify(invoice),
			dataType: "json",
			cache: false,
			timeout: 600000,
			success: function(e) {
				$("#exampleModal").modal("show");
				$("#pdfBtn").click(function() {
					if($("#sendOverEmail").is(":checked")){
						var mailId = $("#emailId").val();
						console.log("mail-> , " , mailId);
						window.location.href = "/admin/invoice/download-pdf/" + mailId + "/" + e;
					}else{
						window.location.href = "/admin/invoice/download-pdf/" + e;
					}
					$("#exampleModal").modal("hide");
				})
				$("#excelBtn").click(function() {
					if($("sendOverEmail").is(":checked")){
						var mailId = $("#emailId").val();
						window.location.href = "/admin/invoice/download-excel/" + mailId + "/" + e;
					}else{
						window.location.href = "/admin/invoice/download-excel/" + e;
					}
					$("#exampleModal").modal("hide");
				})
			},
			error: function(e) {
				console.log(e.responseText);
			}
		})
	}
}


function saveCustomer() {
	var name = $("#name").val();
	var address = $("#address").val();
	var gstNumber = $("#gstNumber").val();
	var vendorCode = $("#vendorCode").val();

	var customer = { "name": name, "address": address, "gstNumber": gstNumber, "vendorCode": vendorCode };

	var url = "/admin/invoice/save-customer";

	var data = customer;

	$.ajax({
		type: "POST",
		contentType: "application/json",
		url: url,
		data: JSON.stringify(data),
		dataType: "json",
		cache: false,
		timeout: 600000,
		success: function(e) {
			$('#customerId').append(new Option(e.name, e.custId));
		},
		error: function(e) {
			console.log(e.responseText);
		}
	})
}

function saveItem() {
	var itemName = $("#itemName").val();
	var hsnCode = $("#hsnCode").val();

	var item = { "itemName": itemName, "hsnCode": hsnCode };
	var url = "/admin/invoice/save-item";
	var data = item;
	$.ajax({
		type: "POST",
		contentType: "application/json",
		url: url,
		data: JSON.stringify(data),
		dataType: "json",
		cache: false,
		timeout: 600000,
		success: function(e) {
			$('#itemId').append(new Option(e.itemName, e.itemId));
		},
		error: function(e) {
			console.log(e.responseText);
		}
	})
}




//refresh the values of total while changing the rate and quantity
var inte;
function refreshContinuosly(item) {
	inte = setInterval(function() {
		calculateTotal(item);
	}, 100);
}



function stopit() {
	clearInterval(inte);
};

