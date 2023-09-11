//customer GetAll

getAllCustomer();

function getAllCustomer() {
    $("#tblCustomer").empty();
    $.ajax({
        url: "http://localhost:8081/10_BackEnd/customer",
        method: 'GET',
        dataType: 'json',
        success: function (resp) {
            console.log(resp.data);

            for (const i of resp.data) {
                let row = `<tr><td>${i.id}</td><td>${i.name}</td><td>${i.address}</td><td>${i.salary}</td></tr>`;
                $("#tblCustomer").append(row);
            }
            if (resp.status == 400) {
                alert(resp.message);
            }

            bindClickEvent()
        },
        error: function (xhr) {
            console.log(xhr);
        }
    });
}

$("#btnGetAll").click(function () {
    getAllCustomer();
});

//customerSave

$("#btnCusSave").click(function () {
    let formData = $("#customerForm").serialize();
    $.ajax({
        url: 'http://localhost:8081/10_BackEnd/customer',
        method: "POST",
        data: formData,


        success: function (resp) {

            if (resp.status == 200) {
                alert(resp.message);
                console.log(resp);
                getAllCustomer();
                console.log(resp);

            } else if (resp.status == 500) {
                alert(resp.message);
            }
        },
        error: function (xhr) {
            console.log(xhr);
        }
    });
});


//customer Delete
$("#btnCusDelete").click(function () {
    let id = $("#txtCustomerID").val();
    $.ajax({
        url: 'http://localhost:8081/10_BackEnd/customer?cusID=' + id,
        method: 'DELETE',

        success: function (resp) {
            if (resp.status == 200) {
                alert(resp.message);
                console.log(resp);
                getAllCustomer();
            } else if (resp.status == 400) {
                console.log(resp);
                alert(resp.message);
            } else {
                alert(resp.message);
                console.log(resp.data);
            }

        },
        error: function (xhr) {
            console.log(xhr);
        }
    });
})
//customer Update
$("#btnUpdate").click(function () {
    let cusOb = {
        "id": "C017",
        "name": "Udith",
        "address": "Kalutara",
        "salary": "345000"
    }

    $.ajax({
        url: "http://localhost:8081/10_BackEnd/customer",
        method: "PUT",
        data: JSON.stringify(cusOb),

        success: function (resp) {
            console.log(resp);
        },
        error: function (xhr) {
            console.log(xhr);
        }

    })
})

function bindClickEvent() {
    $("#tblCustomer>tr").click(function () {
        let id = $(this).children().eq(0).text();
        let name = $(this).children().eq(1).text();
        let address = $(this).children().eq(2).text();
        let salary = $(this).children().eq(3).text();

        $("#txtCustomerID").val(id);
        $("#txtCustomerName").val(name);
        $("#txtCustomerAddress").val(address);
        $("#txtCustomerSalary").val(salary);
    });
}
