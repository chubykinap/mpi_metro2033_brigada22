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
            alert("Не все данные заполнены!");
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
              console.log(data);
              window.location.href = '/posts/show_sensors/' + $("#post_id").val();
            }
          });
          console.log(add_sensor_to_post);
        });
    })
});