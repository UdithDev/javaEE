const BASE_URL = "http://localhost:8081/10_BackEnd/"

//customer GetAll
getAllCustomer();

//add customer Event
$("#btnCusSave").click(function () {
    if (checkAll()) {
        saveCustomer();
    } else {
        alert("Error");
    }
});

//get All customer Event
$("#btnGetAll").click(function () {
    getAllCustomer();
});

//bind event
function bindClick() {
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

//delete btn Event
$("#btnCusDelete").click(function () {
    let id = $("#txtCustomerID").val();

    let consent = confirm("Do you want to delete.?");

    if (consent) {
        let response = deleteCustomer(id);
        if (response) {
            alert("Customer Deleted");
            clearAll();
            getAllCustomer();
        } else {
            alert("Customer Not Removed");
        }
    }
});

//update btn event
$("#btnUpdate").click(function () {
    let id = $("#txtCustomerID").val();
    updateCustomer(id);
    clearAll();
});
//clear btn event
$("#btn-clear1").click(function () {
    clearAll();
});


function saveCustomer() {
    let customerID = $("#txtCustomerID").val();

    if (searchCustomer(customerID.trim()) == undefined) {
        let formData = $("#customerForm").serialize();

        $.ajax({
            url: BASE_URL + 'customer',
            method: "POST",
            data: formData,


            success: function (resp) {
                alert(resp.message);
                clearAll();
                getAllCustomer();
            },
            error: function (error) {
                alert(error.responseJSON.message);
            }
        });
    } else {
        alert("Customer already exits..!");
        clearAll();
    }

}


function getAllCustomer() {
    $("#tblCustomer").empty();
    $.ajax({
        url: BASE_URL + "customer",
        method: 'GET',
        dataType: 'json',
        success: function (resp) {
            //console.log(resp.data);

            for (const i of resp.data) {
                let row = `<tr><td>${i.id}</td><td>${i.name}</td><td>${i.address}</td><td>${i.salary}</td></tr>`;
                $("#tblCustomer").append(row);
            }
            if (resp.status === 400) {
                alert(resp.message);
            }

            bindClick();
        },
        error: function (xhr) {
            alert(xhr.responseJSON.message);
        }
    });
}


//customer Delete
function deleteCustomer(id) {
    $.ajax({
        url: BASE_URL + 'customer?cusID=' + id,
        method: 'DELETE',

        success: function (resp) {
            alert(resp.message);
            getAllCustomer();
            clearAll();
            return true;
        },
        error: function (xhr) {
            alert(xhr.responseJSON.message)
            return false;
        }
    });
}


//customer Search
function searchCustomer(id) {
    let resp = false;
    $.ajax({
        url: BASE_URL + 'customer',
        dataType: "json",

        async: false,

        success: function (response) {
            let customers = response.data;
            resp = customers.find(function (customer) {
                return customer.id == id;
            });
        },
        error: function (error) {
            resp = false;
            alert(error.responseJSON.message);
        }
    });
    return resp;
}


//customer Update
function updateCustomer(id) {
    if (searchCustomer(id) == undefined) {
        alert("No such Customer...please check the ID");
    } else {
        let consent = confirm("DO you really want to update this customer ?");

        if (consent) {
            let customer = searchCustomer(id);

            let name = $("#txtCustomerName").val();
            let address = $("#txtCustomerAddress").val();
            let salary = $("#txtCustomerSalary").val();

            customer.id = id;
            customer.name = name;
            customer.address = address;
            customer.salary = salary;


            $.ajax({
                url: BASE_URL + "customer",
                method: "PUT",
                contentType: "application/json",
                data: JSON.stringify(customer),


                success: function (resp) {
                    alert(resp.message);
                    getAllCustomer();
                    clearAll();

                },
                error: function (error) {
                    alert(error.responseJSON.message);
                }

            });

        }
    }
}

function clearAll(){
    $("#txtCustomerID").val("");
    $("#txtCustomerName").val("");
    $("#txtCustomerAddress").val("");
    $("#txtCustomerSalary").val("");
}

