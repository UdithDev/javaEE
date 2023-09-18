const BASE_URL_2 = "http://localhost:8081/10_BackEnd/"


getAllItem();

function getAllItem() {
    $("#tblItem").empty();
    $.ajax({
        url: BASE_URL_2 + "item",
        method: 'GET',
        dataType: 'json',
        success: function (resp) {


            for (const i of resp.data) {
                let row = `<tr><td>${i.code}</td><td>${i.description}</td><td>${i.itemQty}</td><td>${i.unitPrice}</td></tr>`;
                $("#tblItem").append(row);
            }
            if (resp.status === 400) {
                alert(resp.message);
            }

            bindClickEvent();

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
        url: BASE_URL_2 + "item",
        method: "POST",
        data: formData,

        success: function (resp) {
            console.log(resp);

            if (resp.status === 200) {
                alert(resp.message);
                getAllItem();

            } else if (resp.status === 500) {
                alert(resp.message);
            }
        },
        error: function (xhr, ob) {
            console.log(xhr);
            console.log(ob);
        }
    });
});

//Delete Item

$("#btnItemDelete").click(function () {
    let code = $("#itemCode").val();
    $.ajax({
        url: BASE_URL_2 + 'item?code=' + code,
        method: "DELETE",

        success: function (resp) {
            console.log(resp);

            if (resp.status === 200) {
                alert(resp.message);
                getAllItem();
            } else if (resp.status === 400) {
                alert(resp.message);
            } else {
                alert(resp.message);
                console.log(resp.data);
            }
        },
        error: function (xhr) {
            console.log(xhr);

        }
    })
});

//update Item

$("#btnItemUpdate").click(function () {

    let itemOb = {
        code: $("#itemCode").val(),
        description: $("#itemName").val(),
        itemQty: $("#itemQty").val(),
        unitPrice: $("#itemPrice").val()
    }


    $.ajax({
        url: BASE_URL_2 + "item",
        method: "PUT",
        contentType: "application/json",
        data: JSON.stringify(itemOb),

        success: function (resp) {

            alert(resp.message);
            getAllItem();
            /* if (resp.status === 200) {
                 alert(resp.message);
                 console.log(resp);
                 getAllItem();
             } else if (resp.status === 400) {
                 alert(resp.message);
                 console.log(resp.data);
             } else {
                 alert(resp.message);
                 console.log(resp.data)
             }*/
        },
        error: function (xhr) {
            console.log(xhr);
        }
    });
});


function bindClickEvent() {
    $("#tblItem> tr").click(function () {
        let code = $(this).children().eq(0).text();
        let description = $(this).children().eq(1).text();
        let qty = $(this).children().eq(2).text();
        let unitPrice = $(this).children().eq(3).text();

        $("#itemCode").val(code);
        $("#itemName").val(description);
        $("#itemQty").val(qty);
        $("#itemPrice").val(unitPrice);
    });
}