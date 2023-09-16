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
        url: "http://localhost:8081/10_BackEnd/item",
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
        url: 'http://localhost:8081/10_BackEnd/item?code=' + code,
        method: "DELETE",

        success: function (resp) {
            console.log(resp);

            if(resp.status===200){
                alert(resp.message);
                getAllItem();
            }
            else if(resp.status===400){
                alert(resp.message);
            }
            else {
                alert(resp.message);
                console.log(resp.data);
            }
        },
        error: function (xhr) {
            console.log(xhr);

        }
    })
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