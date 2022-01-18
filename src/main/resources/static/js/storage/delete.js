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
        //var columns = $(this).closest('tr').find('td');
        var tr = $(this).closest('tr');
        var item_id = $(this).closest('a').attr("value");

        if(item_id == ""){
            alert("Предмет не найден!");
            return;
        }

        if (window.confirm("Удалить предмет?")){
            //var item_id = columns[0].innerHTML;
            var item = {
                "item_id":item_id
            };

            $.ajax({
                type: "POST",
                url: "/storage/delete",
                contentType: "application/json",
                data: JSON.stringify(item),
                // dataType: "dataType",
                success: function (data) {
                  console.log("success");
                  if (data.status == "Error"){
                    alert(data.data);
                    return;
                  }else{
                    show_notification(data.data);
                    tr.remove();
                  }
                  console.log(data);
                  //window.location.href = '/storage';
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