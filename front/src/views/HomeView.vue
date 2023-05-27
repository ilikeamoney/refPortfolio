<script setup lang="ts">
import axios from "axios";
import {ref} from "vue";
import {useRouter} from "vue-router";

const router = useRouter()

const posts = ref([]);

axios.get("/api/posts?page=1&size=5").then((response) => {
  response.data.forEach((r: any) => {
    posts.value.push(r);
  });
});

</script>

<template>
  <ul>
    <li v-for="post in posts" :key="post.id">
      <div class="title">
        <router-link :to="{name: 'read', params: {postId : post.id}}">{{
            post.title
          }}</router-link>
      </div>

      <div class="content">
        {{post.content}}
      </div>

      <div class="sub d-flex">
        <div class="category">글 작성일 20:40:55</div>
        <div class="regDate">2023-05-28</div>
      </div>
    </li>
  </ul>
</template>

<style scoped lang="scss">
ul {
  list-style: none;
  padding: 0;

  li {
    margin-bottom: 1.8rem;

    .title {
      a {
        font-size: 1.3rem;
        color: #383838;
        text-decoration: none;
      }

      &:hover {
        text-decoration: underline;
      }
    }

    .content {
      font-size: 0.95rem;
      margin-top: 8px;
      color: #656565;
      white-space: break-spaces;
      line-height: 1.5;
    }

    &:last-child {
      margin-bottom: 0;
    }

    .sub {
      margin-top: 10px;
      font-size: 0.78rem;

      .regDate {
        margin-left: 10px;
        color: #393a34;
      }
    }
  }
}

</style>
