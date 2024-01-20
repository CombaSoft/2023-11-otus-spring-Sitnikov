merge into authors t
using
(
  select
     1 as id,
     'Author_1' as full_name
  from dual

  union all

  select
     2 as id,
     'Author_2' as full_name
  from dual

  union all

  select
     3 as id,
     'Author_3' as full_name
  from dual
) v
on
(
  t.id = v.id
)
when matched then update set full_name = v.full_name
when not matched then insert (id, full_name)
values (v.id, v.full_name);

insert into genres(name)
values ('Genre_1'), ('Genre_2'), ('Genre_3');

insert into books(title, author_id, genre_id)
values ('BookTitle_1', 1, 1), ('BookTitle_2', 2, 2), ('BookTitle_3', 3, 3);