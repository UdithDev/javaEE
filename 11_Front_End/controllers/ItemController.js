getAllItem();
$("#itemGetAll").click(function () {
    getAllItem();
});

function getAllItem() {
    $.ajax({
        url: "http://localhost:8081/10_BackEnd/item",
        method: "GET",
        dataType: 'json',

        success: function (resp) {
            console.log(resp.data);
            for (const i of resp.data) {
                let row = `<tr><td>${i.code}</td><td>${i.description}</td><td>${i.itemQty}</td><td>${i.unitPrice}</td></tr>`
                $("#tblItem").append(row);
            }
        },
        error: function (xhr) {
            console.log(xhr);
        }
    });
}

// item Save

$("#btnSaveItem").click(function () {
    let formData = $("#itemForm").serialize();
    $.ajax({
        url: "http://localhost:8081/10_BackEnd/item",
        method: 'post',
        data: formData,

        success: function (resp) {
            if (resp.status == 200) {
                alert(resp.message);
                getAllItem();
            }

            getAllItem();

        },
        error: function (xhr) {
            console.log(xhr);
        }
    });
});