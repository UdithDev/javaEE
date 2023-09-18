loadAllItems();
loadAllCustomers();
setDates();
searchCustomer();

function loadAllItems() {
    $("#selectItemCode").empty();
    $.ajax({
        url: BASE_URL + 'item',

        success: function (resp) {
            for (let c of resp.data) {
                let code = c.code;
                $("#selectItemCode").append(`<option value="${code}">${code}</option>`);
            }
        },
        error: function (error) {
            let message = JSON.parse(error.responseText).message;
            alert(message);
        }
    });
}

function loadAllCustomers() {
    $("#selectCusID").empty();
    $.ajax({
        url: BASE_URL + "customer",
        dataType: "json",

        success: function (resp) {
            console.log(resp);
            for (let cus of resp.data) {
                $("#selectCusID").append(`<option value="${cus.id}">${cus.id}</option>`);
            }
        }
    });
}

function setDates() {
    let date = new Date().toJSON().split("T")[0];
    $("#txtDate").val(date);
}

function searchCustomer(cusID) {
    let response = "";
    $.ajax({
        url: BASE_URL + "customer",
        dataType: "json",
        async: false,
        headers:{
            Auth:"user=admin,pass=admin"
        },
        success: function (resp) {
            response = resp.data.filter((c) => {
                return c.id == cusID;
            });
        }
    });
    return response;
}


