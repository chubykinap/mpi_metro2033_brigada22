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