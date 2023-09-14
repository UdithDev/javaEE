$("#itemGetAll").click(function () {
    $.ajax({
        url: "http://localhost:8081/10_BackEnd/item",
        method: "GET",


        success: function (resp) {
            console.log(resp);
        },
        error: function (xhr) {
            console.log(xhr);
        }
    });
});