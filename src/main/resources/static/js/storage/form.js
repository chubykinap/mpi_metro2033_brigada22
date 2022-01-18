$(document).ready(function() {
    $(function() {
        $("#create").click(function(e) {
            e.preventDefault();
            var check = false;

            check = validate_name();
            check = validate_quantity();

            if (check){
                alert("Ошибка в заполнении полей.");
                return;
            }

            var item = {
                "name":$("#name").val(),
                "quantity":$("#quantity").val(),
            };

            $.ajax({
                type: "POST",
                url: "/storage/create",
                contentType: "application/json",
                data: JSON.stringify(item),
                success: function (response) {
                    console.log("send");
                    console.log(response.status);
                    if (response.status == "Error"){
                        alert(response.data);
                        return;
                    }
                    console.log("success");
                    window.location.href = '/storage';
                }
            });
        });

        $("#name").on('keyup', function(event){
            event.preventDefault();
            validate_name();
        });

        $("#quantity").on('keyup', function(event){
            event.preventDefault();
            validate_quantity();
        });
    })
});

function validate_name(){
    var regExp = new RegExp("^(?=.{1,100}$)(?![_.])(?!.*[_.]{2})[a-zA-ZА-Яа-я0-9]+(?<![_.])$");
    var OK = regExp.exec($("#name").val());
    if(!OK){
        $( "#name_validation" ).text("Не правильно введено название. Оно должно содержать от 1 до 100 симолов. Досутпные символы: английсике и русские буквы, цифры");
        return true;
    }else{
        $( "#name_validation" ).text("");
        return false;
    }
}

function validate_quantity(){
    var quantity = $("#quantity").val();
    if (quantity < 0){
        $( "#quantity_validation" ).text("Введите число большее нуля");
        return true;
    }else{
        $( "#quantity_validation" ).text("");
        return false;
    }
}