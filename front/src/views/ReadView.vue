<script lang="ts">
import {onMounted, ref} from "vue";
import axios from "axios";
import {useRouter} from "vue-router";

export default {
  props: {
    postId: {
      type: [Number, String],
      required: true,
    },
  },
  setup(props) {
    const post = ref({
      id: 0,
      title: "",
      content: "",
    });

    onMounted(() => {
      axios.get(`/api/posts/${props.postId}`).then((response) => {
        post.value = response.data;
      });
    });

    const router = useRouter();

    const moveToEdit = () => {
      router.push({name : "edit", params: { postId: props.postId } });
    }

    return {
      post, moveToEdit
    };
  },
};
</script>

<template>
  <el-row>
    <el-col>
      <h2 class="title">{{post.title}}</h2>
    </el-col>
  </el-row>

  <el-row class="mt-3">
    <el-col>
      <div class="sub d-flex">
        <div class="category">글 작성일</div>
        <div class="regDate">2023-05-28 20:40:55</div>
      </div>

      <div class="content">{{post.content}}</div>
    </el-col>
  </el-row>

  <el-row class="mt-3">
    <el-col>
      <div class="d-flex justify-content-end">
        <el-button type="warning" @click="moveToEdit()">수정</el-button>
      </div>
    </el-col>
  </el-row>
</template>

<style scoped lang="scss">
.title {
  font-size: 1.6rem;
  font-weight: 600;
  color: #383838;
}

.sub {
  margin-top: 0.1px;
  font-size: 0.78rem;

  .regDate {
    margin-left: 10px;
    color: #393a34;
  }
}

.content {
  font-size: 0.95rem;
  margin-top: 40px;
  color: #656565;
  white-space: break-spaces;
  line-height: 1.5;

}
</style>