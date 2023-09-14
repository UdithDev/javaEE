
getAllItem();
$("#itemGetAll").click(function () {
   getAllItem();
});

function  getAllItem(){
    $.ajax({
        url: "http://localhost:8081/10_BackEnd/item",
        method: "GET",
        dataType:'json',


        success: function (resp) {
            console.log(resp.data);
            for (const i of resp.data) {
                let row=`<tr><td>${i.code}</td><td>${i.description}</td><td>${i.itemQty}</td><td>${i.unitPrice}</td></tr>`
                $("#tblItem").append(row);
            }

        },
        error: function (xhr) {
            console.log(xhr);
        }
    });
}