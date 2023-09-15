getAllItem();

function getAllItem() {
    $("#tblItem").empty();
    $.ajax({
        url: "http://localhost:8081/10_BackEnd/item",
        method: 'GET',
        dataType: 'json',
        success: function (resp) {


            for (const i of resp.data) {
                let row = `<tr><td>${i.code}</td><td>${i.description}</td><td>${i.itemQty}</td><td>${i.unitPrice}</td></tr>`;
                $("#tblItem").append(row);
            }
            if (resp.status == 400) {
                alert(resp.message);
            }


        },
        error: function (xhr) {
            console.log(xhr);
        }
    });
}

$("#itemGetAll").click(function () {
    getAllItem();
});


// item Save

$("#btnSaveItem").click(function () {
    let formData = $("#itemForm").serialize();
    $.ajax({
        url: "http://localhost:8081/10_BackEnd/item",
        method: "POST",
        data: formData,

        success: function (resp) {
            console.log(resp);

            if (resp.status === 200) {
                alert(resp.message);
                getAllItem();

            } else if (resp.status ===500) {
                alert(resp.message);
            }
        },
        error: function (xhr, ob) {
            console.log(xhr);
            console.log(ob);
        }
    });
});