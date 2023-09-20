const BASE_URL = "http://localhost:8081/10_BackEnd/"

//customer GetAll

getAllCustomer();

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
        url: BASE_URL + 'customer',
        method: "POST",
        data: formData,


        success: function (resp) {

            if (resp.status === 200) {
                alert(resp.message);
                //console.log(JSON.parse(resp.data));
                getAllCustomer();

            } else if (resp.status === 500) {
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
        url: BASE_URL + 'customer?cusID=' + id,
        method: 'DELETE',

        success: function (resp) {
            if (resp.status === 200) {
                alert(resp.message);
                console.log(resp);
                getAllCustomer();
            } else if (resp.status === 400) {
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
});


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

//update btn event
$("#btnUpdate").click(function () {
    let id = $("#txtCustomerID").val();
    updateCustomer(id);
});

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

                    if (resp.status === 200) {
                        alert(resp.message);
                        console.log(resp);
                        getAllCustomer();
                    } else if (resp.status === 400) {
                        alert(resp.message);
                        console.log(resp.data);
                    } else if (resp.status === 500) {
                        alert(resp.message);
                        console.log(resp.data);
                    } else {
                        alert(resp.message);
                        console.log(resp.data)
                    }

                },
                error: function (ob, status, t) {
                    console.log(ob);
                    console.log(status);
                    console.log(t);
                }

            });

        }
    }


}

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
