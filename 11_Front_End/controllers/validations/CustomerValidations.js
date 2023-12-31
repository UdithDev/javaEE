//validation for customers
const CUS_ID_REGEX = /^(C00-)[0-9]{3}$/;
const CUS_NAME_REGEX = /^[A-Za-z ]{5,}$/;
const CUS_ADDRESS_REGEX = /^[A-Za-z0-9 ]{8,}$/;
const CUS_SALARY_REGEX = /^[0-9]{2,}([.][0-9]{2})?$/;


let c_vArray = new Array();
c_vArray.push({field: $("#txtCustomerID"), regEX: CUS_ID_REGEX});
c_vArray.push({field: $("#txtCustomerName"), regEX: CUS_NAME_REGEX});
c_vArray.push({field: $("#txtCustomerAddress"), regEX: CUS_ADDRESS_REGEX});
c_vArray.push({field: $("#txtCustomerSalary"), regEX: CUS_SALARY_REGEX});

/*
function clearCustomerInputFields() {
    $("#txtCustomerID,#txtCustomerName,#txtCustomerAddress,#txtCustomerSalary").val("");
    $("#txtCustomerID,#txtCustomerName,#txtCustomerAddress,#txtCustomerSalary").css("border", "1px solid #ced4da");
    $("#txtCustomerID").focus();
    setBtn();
}*/

setBtn();

//disable tab
$("#txtCustomerID, #txtCustomerName, #txtCustomerAddress, #txtCustomerSalary ").on("keydown keyup", function (e) {
    let indexNo = c_vArray.indexOf(c_vArray.find((c) => c.field.attr("id") == e.target.id));

    if (e.key == "Tab") {
        e.preventDefault();
    }

    //check validations
    checkValidation(c_vArray[indexNo]);

    setBtn();

    //If the enter key pressed cheque and focus

    if (e.key == "Enter") {
        if (e.target.id != c_vArray[c_vArray.length - 1].field.attr("id")) {

            //check validation is ok
            if (checkValidation(c_vArray[indexNo])) {
                c_vArray[indexNo + 1].field.focus();
            }
        } else {
            if (checkValidation(c_vArray[indexNo])) {
                saveCustomer();
            }
        }
    }
});

function checkValidation(object) {
    if (object.regEX.test(object.field.val())) {
        setBorder(true, object)
        return true;
    }

    setBorder(false, object)
    return false;
}

function setBorder(bol, ob) {
    if (!bol) {
        if (ob.field.val().length >= 1) {
            ob.field.css("border", "2px solid red");
        } else {
            ob.field.css("border", "1px solid #ced4da");
        }
    } else {
        if (ob.field.val().length >= 1) {
            ob.field.css("border", "2px solid green");
        } else {
            ob.field.css("border", "1px solid #ced4da")
        }
    }
}

function checkAll() {
    for (let i = 0; i < c_vArray.length; i++) {
        if (!checkValidation(c_vArray[i])) return false;
    }
    return true;
}

function setBtn() {
    $("#btnCusDelete").prop("disabled", true);
    $("#btnUpdate").prop("disabled", true);

    if (checkAll()) {
        $("#btnCusSave").prop("disabled", false);
    } else {
        $("#btnCusSave").prop("disabled", true);
    }

    let id = $("#txtCustomerID").val();
    if (searchCustomer(id) == undefined) {
        $("#btnCusDelete").prop("disabled", true);
        $("#btnUpdate").prop("disabled", false);
    }
}