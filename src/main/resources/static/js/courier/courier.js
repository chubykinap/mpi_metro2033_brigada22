$(document).ready(function() {

    $(document).ready(function() {
        $(function(){

          var page_size = $("#page_size").text();
          var start_page = $("#start_page").text();

          console.log(page_size);
          console.log(start_page);

          var start_index = page_size * (start_page - 1) + 1;

          var table = document.getElementsByTagName('table')[0],
          rows = table.getElementsByTagName('tr'),
          text = 'textContent' in document ? 'textContent' : 'innerText';

          var j = start_index;
          for (var i = 1, len = rows.length; i < len; i++) {
            rows[i].children[0][text] = j + rows[i].children[0][text];
            j++;
          }
        })
    });

    $(document).on('click', '#delete', function(event) {
        event.preventDefault();

        var tr = $(this).closest('tr');
        var courier_id = $(this).closest('a').attr("value");

//        var tr = $(this).closest('tr');
//        var columns = $(this).closest('tr').find('td');

        if (window.confirm("Удалить курьера?")){
            console.log(courier_id);

            if (courier_id == '') {
              alert("Курьер не найден");
              return;
            }

          var courier = {
            "courier_id": courier_id
          };

            $.ajax({
                type: "POST",
                url: "/courier/delete",
                contentType: "application/json",
                data: JSON.stringify(courier),
                success: function (data) {
                  console.log("success");
                  console.log(data);

                  if (data.status == "Error"){
                    alert(data.data);
                    return;
                  }
                  else{
                    show_notification(data.data);
                    tr.remove();
                  }
//                  window.location.href = '/courier';
                },
                error: function (e){
                    console.log(e);
                }
            });
        }
    });

    function show_notification(info){
     $('#notification').text("Курьер: " + info.login + " " + info.name + " " + info.surname + " удален");
     $('#notification').show();
     $('#notification').delay(7000).hide(0);
    }

});