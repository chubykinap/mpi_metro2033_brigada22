$(document).ready(function() {
    $(function(){

        $("#change").click(function(){

            var check = false;

            check = validate_name();
            check = validate_quantity();
            check = validate_weight();

            if (check){
                alert("Ошибка в заполнении полей.");
                return;
            }

            var item = {
                "item_id":$("#item_id").val(),
                "name":$("#name").val(),
                "quantity":$("#quantity").val(),
                "weight":$("#weight").val()
            };

            $.ajax({
                type: "POST",
                url: "/storage/change",
                contentType: "application/json",
                data: JSON.stringify(item),
                // dataType: "dataType",
                success: function (data) {
                    if (data.status == "Error"){
                        alert(data.data);
                        return;
                    }
                    console.log("success");
                    window.location.href = '/storage';
                }
            });
            console.log(item);
        });

        $("#delete").click(function(){
            if (window.confirm("Удалить предмет?")){
                $.ajax({
                    type: "GET",
                    url: "/storage/delete/" + $("#item_id").val(),
                    success: function (data) {
                      console.log("success");
                      if (data.status == "Error"){
                        alert(data.data);
                        return;
                      }
                      console.log(data);
                      window.location.href = '/storage';
                    }
                });
            }
        });

        $("#name").on('keyup', function(event){
            event.preventDefault();
            validate_name();
        });

        $("#quantity").on('keyup', function(event){
            event.preventDefault();
            validate_quantity();
        });

        $("#weight").on('keyup', function(event){
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

function validate_weight(){
    var quantity = $("#weight").val();
    if (quantity < 0){
        $( "#weight_validation" ).text("Введите число большее нуля");
        return true;
    }else{
        $( "#weight_validation" ).text("");
        return false;
    }
}