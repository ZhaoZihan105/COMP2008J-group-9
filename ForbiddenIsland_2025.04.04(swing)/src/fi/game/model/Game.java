package fi.game.model;

import java.util.*;

/**
 * 游戏主类，协调所有游戏活动
 */
public class Game {
    private GameData gameData;
    private List<Player> players;
    private WaterMeter waterMeter;
    private Player currentPlayer;
    private int currentPlayerIndex;
    private int actionsRemaining;
    private GamePhase currentPhase;
    private boolean gameOver;
    private boolean gameWon;

    // 用于跟踪选择
    private Tile selectedTile;
    private Player selectedPlayer;
    private TreasureCard selectedCard;
    private List<Player> selectedPlayers;

    public Game() {
        gameData = new GameData();
        players = new ArrayList<>();
        waterMeter = new WaterMeter();
        gameOver = false;
        gameWon = false;
        currentPhase = GamePhase.SETUP;
        selectedPlayers = new ArrayList<>();
    }

    public void initialize(int numPlayers, int difficultyLevel) {
        // 重置游戏状态
        gameData = new GameData();
        players.clear();
        waterMeter = new WaterMeter();
        gameOver = false;
        gameWon = false;
        selectedTile = null;
        selectedPlayer = null;
        selectedCard = null;
        selectedPlayers.clear();

        // 设置难度等级
        waterMeter.setLevel(difficultyLevel);

        // 创建岛屿
        gameData.createIsland();

        // 创建并洗牌
        gameData.createTreasureDeck();
        gameData.createFloodDeck();

        // 给玩家分配角色
        assignRoles(numPlayers);

        // 发初始卡牌
        dealInitialCards();

        // 淹没初始瓦片
        floodInitialTiles();

        // 设置第一个玩家
        currentPlayerIndex = 0;
        currentPlayer = players.get(currentPlayerIndex);
        actionsRemaining = 3;
        currentPhase = GamePhase.PLAYER_ACTIONS;
    }

    private void assignRoles(int numPlayers) {
        // 随机分配角色给玩家
        List<Role> roles = new ArrayList<>(Arrays.asList(Role.values()));
        Collections.shuffle(roles);

        for (int i = 0; i < numPlayers; i++) {
            Player player = new Player(roles.get(i));
            players.add(player);

            // 将玩家放在起始瓦片上
            Tile startingTile = gameData.getStartingTile(player.getRole());
            player.setCurrentTile(startingTile);
            startingTile.addPlayer(player);
        }
    }

    private void dealInitialCards() {
        // 给每个玩家发2张宝藏卡
        for (Player player : players) {
            for (int i = 0; i < 2; i++) {
                TreasureCard card = gameData.drawTreasureCard();
                // 如果抽到Waters Rise卡，放回并重新抽
                while (card != null && card.getType() == CardType.WATERS_RISE) {
                    gameData.discardTreasureCard(card);
                    gameData.getTreasureDeck().reshuffleDiscardPile();
                    card = gameData.drawTreasureCard();
                }

                if (card != null) {
                    player.addCard(card);
                }
            }
        }
    }

    private void floodInitialTiles() {
        // 淹没6个初始瓦片
        for (int i = 0; i < 6; i++) {
            FloodCard card = gameData.drawFloodCard();
            if (card != null) {
                gameData.floodTile(card.getTileType());
                gameData.discardFloodCard(card);
            }
        }
    }

    // 玩家行动方法

    public boolean movePlayer(Player player, Tile destination) {
        if (currentPhase != GamePhase.PLAYER_ACTIONS || actionsRemaining <= 0) {
            return false;
        }

        Tile currentTile = player.getCurrentTile();

        // 检查移动是否有效
        boolean validMove = false;

        // 正常移动：相邻的非沉没瓦片
        if (currentTile.isAdjacent(destination) && destination.getState() != TileState.SUNK) {
            validMove = true;
        }

        // 特殊角色能力
        if (player.getRole() == Role.EXPLORER) {
            // 探险家可以沿对角线移动
            int rowDiff = Math.abs(currentTile.getRow() - destination.getRow());
            int colDiff = Math.abs(currentTile.getCol() - destination.getCol());
            if (rowDiff <= 1 && colDiff <= 1 && destination.getState() != TileState.SUNK) {
                validMove = true;
            }
        } else if (player.getRole() == Role.PILOT) {
            // 飞行员可以飞到任何瓦片（每回合一次）
            if (destination.getState() != TileState.SUNK && !player.hasUsedSpecialAction()) {
                validMove = true;
                player.useSpecialAction();
            }
        } else if (player.getRole() == Role.DIVER) {
            // 潜水员可以穿过一个或多个相邻的沉没或被淹没的瓦片
            // 简化实现：潜水员可以移动到任何非沉没瓦片
            if (destination.getState() != TileState.SUNK) {
                validMove = true;
            }
        }

        if (validMove) {
            // 更新玩家位置
            currentTile.removePlayer(player);
            destination.addPlayer(player);
            player.setCurrentTile(destination);

            actionsRemaining--;
            return true;
        }

        return false;
    }

    public boolean shoreUp(Tile tile) {
        if (currentPhase != GamePhase.PLAYER_ACTIONS || actionsRemaining <= 0) {
            return false;
        }

        if (tile.getState() != TileState.FLOODED) {
            return false;
        }

        Tile playerTile = currentPlayer.getCurrentTile();

        // 检查加固是否有效
        boolean validShoreUp = false;

        // 正常加固：自己的瓦片或相邻瓦片
        if (playerTile == tile || playerTile.isAdjacent(tile)) {
            validShoreUp = true;
        }

        // 特殊角色能力
        if (currentPlayer.getRole() == Role.EXPLORER) {
            // 探险家可以沿对角线加固
            int rowDiff = Math.abs(playerTile.getRow() - tile.getRow());
            int colDiff = Math.abs(playerTile.getCol() - tile.getCol());
            if (rowDiff <= 1 && colDiff <= 1) {
                validShoreUp = true;
            }
        }

        if (validShoreUp) {
            tile.shoreUp();

            // 工程师可以用1个行动加固2个瓦片
            if (currentPlayer.getRole() != Role.ENGINEER) {
                actionsRemaining--;
            }

            return true;
        }

        return false;
    }

    public boolean giveCard(Player giver, Player receiver, TreasureCard card) {
        if (currentPhase != GamePhase.PLAYER_ACTIONS || actionsRemaining <= 0) {
            return false;
        }

        // 检查给卡牌是否有效
        boolean validGive = false;

        // 正常给卡：两个玩家在同一瓦片上且不是特殊行动卡
        if (giver.getCurrentTile() == receiver.getCurrentTile() &&
                card.getType() != CardType.HELICOPTER &&
                card.getType() != CardType.SANDBAG &&
                card.getType() != CardType.WATERS_RISE) {
            validGive = true;
        }

        // 特殊角色能力
        if (giver.getRole() == Role.MESSENGER) {
            // 信使可以在不在同一瓦片的情况下给卡牌
            if (card.getType() != CardType.HELICOPTER &&
                    card.getType() != CardType.SANDBAG &&
                    card.getType() != CardType.WATERS_RISE) {
                validGive = true;
            }
        }

        if (validGive) {
            // 检查接收者的手牌数量
            if (receiver.getHand().size() < 5) {
                giver.removeCard(card);
                receiver.addCard(card);
                actionsRemaining--;
                return true;
            } else {
                // 接收者手牌已满
                return false;
            }
        }

        return false;
    }

    public boolean captureTreasure(Player player, Treasure treasure) {
        if (currentPhase != GamePhase.PLAYER_ACTIONS || actionsRemaining <= 0) {
            return false;
        }

        // 检查是否可以捕获宝藏
        if (player.canCaptureTreasure(treasure)) {
            CardType cardType = treasure.toCardType();

            // 丢弃4张相应的宝藏卡
            List<TreasureCard> cardsToDiscard = player.getCardsOfType(cardType);
            for (int i = 0; i < 4 && i < cardsToDiscard.size(); i++) {
                TreasureCard card = cardsToDiscard.get(i);
                player.removeCard(card);
                gameData.discardTreasureCard(card);
            }

            // 捕获宝藏
            gameData.captureTreasure(treasure);
            actionsRemaining--;
            return true;
        }

        return false;
    }

    public void drawTreasureCards() {
        if (currentPhase != GamePhase.DRAW_TREASURE_CARDS) {
            return;
        }

        // 抽2张宝藏卡
        for (int i = 0; i < 2; i++) {
            TreasureCard card = gameData.drawTreasureCard();

            if (card != null) {
                if (card.getType() == CardType.WATERS_RISE) {
                    // 水位上升
                    waterMeter.rise();
                    gameData.reshuffleFloodDiscard();
                    gameData.discardTreasureCard(card);
                } else {
                    // 检查是否超过手牌上限
                    if (currentPlayer.getHand().size() < 5) {
                        currentPlayer.addCard(card);
                    } else {
                        // 需要丢弃一张卡
                        // 在实际游戏中，玩家需要选择丢弃哪张卡
                        // 这里简化处理
                        gameData.discardTreasureCard(card);
                    }
                }
            }
        }

        // 进入下一阶段
        currentPhase = GamePhase.DRAW_FLOOD_CARDS;
    }

    public void drawFloodCards() {
        if (currentPhase != GamePhase.DRAW_FLOOD_CARDS) {
            return;
        }

        // 根据水位抽取洪水卡
        int numCards = waterMeter.getNumFloodCards();

        for (int i = 0; i < numCards; i++) {
            FloodCard card = gameData.drawFloodCard();

            if (card != null) {
                TileType tileType = card.getTileType();
                Tile tile = gameData.getTile(tileType);

                if (tile != null) {
                    if (tile.getState() == TileState.NORMAL) {
                        // 正常瓦片变为被淹没
                        tile.flood();
                    } else if (tile.getState() == TileState.FLOODED) {
                        // 被淹没瓦片沉没
                        tile.sink();

                        // 处理玩家需要移动的情况
                        handlePlayerEvacuation(tile);
                    }
                }

                gameData.discardFloodCard(card);
            }
        }

        // 检查游戏结束条件
        if (checkGameOver()) {
            currentPhase = GamePhase.GAME_OVER;
            return;
        }

        // 进入下一个玩家的回合
        nextPlayer();
    }

    private void handlePlayerEvacuation(Tile sunkTile) {
        // 处理瓦片沉没时玩家需要撤离的情况
        List<Player> playersToEvacuate = new ArrayList<>(sunkTile.getPlayersOnTile());

        for (Player player : playersToEvacuate) {
            // 寻找可移动的相邻瓦片
            List<Tile> availableTiles = new ArrayList<>();

            for (Tile adjacentTile : sunkTile.getAdjacentTiles()) {
                if (adjacentTile.getState() != TileState.SUNK) {
                    availableTiles.add(adjacentTile);
                }
            }

            // 特殊角色能力
            if (player.getRole() == Role.DIVER) {
                // 潜水员可以游到最近的瓦片
                // 简化：添加所有非沉没瓦片作为可选项
                for (Tile tile : gameData.getIsland()) {
                    if (tile.getState() != TileState.SUNK && !availableTiles.contains(tile)) {
                        availableTiles.add(tile);
                    }
                }
            } else if (player.getRole() == Role.EXPLORER) {
                // 探险家可以沿对角线游动
                // 寻找对角线相邻的瓦片
                for (Tile tile : gameData.getIsland()) {
                    int rowDiff = Math.abs(sunkTile.getRow() - tile.getRow());
                    int colDiff = Math.abs(sunkTile.getCol() - tile.getCol());
                    if (rowDiff <= 1 && colDiff <= 1 && tile.getState() != TileState.SUNK && !availableTiles.contains(tile)) {
                        availableTiles.add(tile);
                    }
                }
            }

            if (availableTiles.isEmpty()) {
                // 玩家无处可逃，游戏结束
                gameOver = true;
                gameWon = false;
            } else {
                // 在实际游戏中，玩家需要选择移动到哪个瓦片
                // 这里简化处理，自动选择第一个可用瓦片
                Tile evacuationTile = availableTiles.get(0);
                evacuationTile.addPlayer(player);
                player.setCurrentTile(evacuationTile);
            }
        }
    }

    public boolean useHelicopterLift(Player player, TreasureCard helicopterCard, Tile destination, List<Player> playersToMove) {
        if (helicopterCard.getType() != CardType.HELICOPTER) {
            return false;
        }

        // 检查目的地是否有效
        if (destination.getState() == TileState.SUNK) {
            return false;
        }

        // 检查所有玩家是否都在同一瓦片上（简化规则）
        Tile sourceTile = player.getCurrentTile();
        for (Player p : playersToMove) {
            if (p.getCurrentTile() != sourceTile) {
                return false;
            }
        }

        // 执行直升机行动
        for (Player p : playersToMove) {
            Tile currentTile = p.getCurrentTile();
            currentTile.removePlayer(p);
            destination.addPlayer(p);
            p.setCurrentTile(destination);
        }

        // 丢弃直升机卡
        player.removeCard(helicopterCard);
        gameData.discardTreasureCard(helicopterCard);

        return true;
    }

    public boolean useSandbag(Player player, TreasureCard sandbagCard, Tile tileToShoreUp) {
        if (sandbagCard.getType() != CardType.SANDBAG) {
            return false;
        }

        // 检查瓦片是否被淹没
        if (tileToShoreUp.getState() != TileState.FLOODED) {
            return false;
        }

        // 执行沙袋行动
        tileToShoreUp.shoreUp();

        // 丢弃沙袋卡
        player.removeCard(sandbagCard);
        gameData.discardTreasureCard(sandbagCard);

        return true;
    }

    public boolean liftOff() {
        // 检查是否满足游戏胜利条件：
        // 1. 所有宝藏都已捕获
        // 2. 所有玩家都在愚者着陆点
        // 3. 有人有直升机卡

        if (!gameData.areAllTreasuresCaptured()) {
            return false;
        }

        Tile foolsLanding = gameData.getTile(TileType.FOOLS_LANDING);
        if (foolsLanding == null || foolsLanding.getState() == TileState.SUNK) {
            return false;
        }

        boolean allPlayersOnFoolsLanding = true;
        boolean someoneHasHelicopterCard = false;

        for (Player player : players) {
            if (player.getCurrentTile() != foolsLanding) {
                allPlayersOnFoolsLanding = false;
                break;
            }

            if (player.hasCard(CardType.HELICOPTER)) {
                someoneHasHelicopterCard = true;
            }
        }

        if (allPlayersOnFoolsLanding && someoneHasHelicopterCard) {
            gameOver = true;
            gameWon = true;
            return true;
        }

        return false;
    }

    public boolean checkGameOver() {
        // 检查游戏失败条件：
        // 1. 如果两个相同宝藏的瓦片都沉没，且宝藏未被捕获
        // 2. 愚者着陆点已沉没
        // 3. 任何玩家溺水（无处可逃）
        // 4. 水位达到最高点

        for (Treasure treasure : Treasure.values()) {
            if (!gameData.isTreasureCaptured(treasure) && gameData.areTreasureTilesSunk(treasure)) {
                gameOver = true;
                gameWon = false;
                return true;
            }
        }

        if (gameData.isFoolsLandingSunk()) {
            gameOver = true;
            gameWon = false;
            return true;
        }

        if (waterMeter.isGameOver()) {
            gameOver = true;
            gameWon = false;
            return true;
        }

        return false;
    }

    public void nextPlayer() {
        // 重置当前玩家的特殊行动状态
        currentPlayer.resetSpecialAction();

        // 清除选择
        selectedTile = null;
        selectedPlayer = null;
        selectedCard = null;
        selectedPlayers.clear();

        // 进入下一个玩家的回合
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        currentPlayer = players.get(currentPlayerIndex);
        actionsRemaining = 3;
        currentPhase = GamePhase.PLAYER_ACTIONS;
    }

    public void nextPhase() {
        switch (currentPhase) {
            case PLAYER_ACTIONS:
                currentPhase = GamePhase.DRAW_TREASURE_CARDS;
                drawTreasureCards();
                break;
            case DRAW_TREASURE_CARDS:
                currentPhase = GamePhase.DRAW_FLOOD_CARDS;
                drawFloodCards();
                break;
            case DRAW_FLOOD_CARDS:
                nextPlayer();
                break;
            case SPECIAL_ACTION:
                // 根据特殊行动的类型返回到适当的阶段
                // 这里简化处理，直接进入洪水卡阶段
                currentPhase = GamePhase.DRAW_FLOOD_CARDS;
                break;
            case GAME_OVER:
                // 游戏结束，不做任何操作
                break;
            default:
                break;
        }
    }

    // Getters and Setters

    public GameData getGameData() {
        return gameData;
    }

    public List<Player> getPlayers() {
        return new ArrayList<>(players);
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player player) {
        this.currentPlayer = player;
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).equals(player)) {
                currentPlayerIndex = i;
                break;
            }
        }
    }

    public int getActionsRemaining() {
        return actionsRemaining;
    }

    public void setActionsRemaining(int actionsRemaining) {
        this.actionsRemaining = actionsRemaining;
    }

    public GamePhase getCurrentPhase() {
        return currentPhase;
    }

    public void setCurrentPhase(GamePhase currentPhase) {
        this.currentPhase = currentPhase;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public boolean isGameWon() {
        return gameWon;
    }

    public WaterMeter getWaterMeter() {
        return waterMeter;
    }

    public Tile getSelectedTile() {
        return selectedTile;
    }

    public void setSelectedTile(Tile tile) {
        this.selectedTile = tile;
    }

    public Player getSelectedPlayer() {
        return selectedPlayer;
    }

    public void setSelectedPlayer(Player player) {
        this.selectedPlayer = player;
    }

    public TreasureCard getSelectedCard() {
        return selectedCard;
    }

    public void setSelectedCard(TreasureCard card) {
        this.selectedCard = card;
    }

    public List<Player> getSelectedPlayers() {
        return new ArrayList<>(selectedPlayers);
    }

    public void addSelectedPlayer(Player player) {
        if (!selectedPlayers.contains(player)) {
            selectedPlayers.add(player);
        }
    }

    public void removeSelectedPlayer(Player player) {
        selectedPlayers.remove(player);
    }

    public void clearSelectedPlayers() {
        selectedPlayers.clear();
    }

    public void clearSelections() {
        selectedTile = null;
        selectedPlayer = null;
        selectedCard = null;
        selectedPlayers.clear();
    }
}