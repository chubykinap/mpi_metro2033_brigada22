$(document).ready(function() {
    $(function() {
        $("#save").click(function() {
            var send_state = {
                "id":$("#order_id").val(),
                "state":$("#state").val()
            };
            alert("Установлен новый статус: " + $("#state").val())
            console.log($("#state").html());
            $.ajax({
                type: "POST",
                url: "/delivery/changeState",
                contentType: "application/json",
                data: JSON.stringify(send_state),
                // dataType: "dataType",
                success: function (data) {
                  console.log("success");
                  window.location.href = '/delivery';
                }
            });
        });

        $("#delete").click(function(){
            if (!window.confirm("Удалить заказ?")){
                return;
            }else{
                $.ajax({
                    type: "POST",
                    url: "/delivery/delete/" + $("#text_id").text(),
                    success: function (data) {
                      console.log("success");
                      console.log(data);
                      window.location.href = '/delivery';
                    }
                });
            }
        });
    })
});