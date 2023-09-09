
//customer GetAll

getAllCustomer();
function getAllCustomer(){
    $("#tblCustomer").empty();
    $.ajax({
        url: "http://localhost:8081/10_BackEnd/customer",
        method: 'GET',
        dataType:'json',
        success: function (resp) {
            console.log(resp.data);

            for (const i of resp.data) {
                let row=`<tr><td>${i.id}</td><td>${i.name}</td><td>${i.address}</td><td>${i.salary}</td></tr>`;
                $("#tblCustomer").append(row);
            }
            if(resp.status==400){
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



function bindClickEvent(){
    $("#tblCustomer>tr").click( function (){
        let id= $(this).children().eq(0).text();
        let name = $(this).children().eq(1).text();
        let address = $(this).children().eq(2).text();
        let salary = $(this).children().eq(3).text();

        $("#txtCustomerID").val(id);
        $("#txtCustomerName").val(name);
        $("#txtCustomerAddress").val(address);
        $("#txtCustomerSalary").val(salary);
    });
}
