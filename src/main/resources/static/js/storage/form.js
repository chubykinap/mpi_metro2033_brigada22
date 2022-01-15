$(document).ready(function() {
    $(function() {
        $("#create").click(function() {
            var regExp = new RegExp("^(?=.{1,100}$)(?![_.])(?!.*[_.]{2})[a-zA-ZА-Яа-я0-9]+(?<![_.])$");
            var OK = regExp.exec($("#name").val());
            if(!OK){
                $( "#name_validation" ).text("Не правильно введено название. Оно должно содержать от 1 до 100 симолов. Досутпные символы: английсике и русские буквы, цифры");
                return;
            }else{
                $( "#name_validation" ).text("");
            }


        });
    })
});