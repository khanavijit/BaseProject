CREATE TABLE Test.blog_image
(
    imgId      text ,
    blogId      text ,
    path   text,
    PRIMARY KEY(blogId, imgId)
);