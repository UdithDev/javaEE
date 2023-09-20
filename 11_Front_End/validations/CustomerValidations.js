//validation for customers
const CUS_ID_REGEX = /^(C00-)[0-9]{3}$/;
const CUS_NAME_REGEX = /^[A-Za-z ]{5,}$/;
const CUS_ADDRESS_REGEX = /^[A-Za-z0-9 ]{8,}$/;
const CUS_SALARY_REGEX = /^[0-9]{2,}([.][0-9]{2})?$/;


let c_vArray = new Array();
c_vArray.push({field:$("#txtCustomerID"), regEX: CUS_ID_REGEX});
c_vArray.push({field:$("#txtCustomerName"), regEX: CUS_NAME_REGEX});
c_vArray.push({field:$("#txtCustomerAddress"), regEX: CUS_ADDRESS_REGEX});
c_vArray.push({field:$("#txtCustomerSalary"), regEX: CUS_SALARY_REGEX});


function clearCustomerInputFields(){
    $("#txtCustomerID,#txtCustomerName,#txtCustomerAddress,#txtCustomerSalary").val("");
    $("#txtCustomerID,#txtCustomerName,#txtCustomerAddress,#txtCustomerSalary").css("border", "1px solid #ced4da");
    $("#txtCustomerID").focus();
    setBtn();
}

setBtn();

//disable tab