$(document).ready(function() {
    $(function(){

        $("#change").click(function(){

          check = false;

          $('#ajax_change_post *').filter(':input').each(function(){
              if($(this).val() == ''){
                check = true;
              }
          });

          if (check == true){
            alert("Виберите датчик!");
            return;
          }

          var add_sensor_to_post = {
            "post_id":$("#post_id").val(),
            "sensor_id":$("#sensor").val()
          };

          $.ajax({
            type: "POST",
            url: "/posts/add_sensor_to_post",
            contentType: "application/json",
            data: JSON.stringify(add_sensor_to_post),
            // dataType: "dataType",
            success: function (data) {
              console.log("success");
              if (data.status == "Error"){
                alert(data.data);
                return;
              }
              else{
                show_notification(data.data);
                  $("#sensor option[value=" + $("#sensor").val() + "]").remove();
                  console.log($("#sensor option").length);
                  if($("#sensor option").length <= 1){
                      fromReset();
                  }
              }
//              window.location.href = '/posts/show_sensors/' + $("#post_id").val();
            },
             error: function (e){
                 console.log(e);
             }
          });
          console.log(add_sensor_to_post);
        });
    })

     function show_notification(info){
        $('#notification').text("Датчик:  " + info.name + " " + info.location + " добавлен на пост");
        $('#notification').show();
        $('#notification').delay(7000).hide(0);
     }

      function fromReset() {
          document.getElementById("ajax_change_post").reset();
          var elem = document.getElementById("create_div");
          elem.parentNode.removeChild(elem);
          document.getElementById("no_sensors_div").style.visibility = "visible";
          $('#no_sensors_div').append("<h4>Нет не активных датчиков</h4>");
      }

});